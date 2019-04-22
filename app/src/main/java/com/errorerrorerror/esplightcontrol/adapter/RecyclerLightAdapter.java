package com.errorerrorerror.esplightcontrol.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.errorerrorerror.esplightcontrol.databinding.LightRecyclerviewBinding;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.rxobservable.RxIOSStyleSlider;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RecyclerLightAdapter extends ListAdapter<Devices, LightViewHolder> {

    private static final DiffUtil.ItemCallback<Devices> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Devices>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Devices oldItem, @NonNull Devices newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Devices oldItem,
                                                  @NonNull Devices newItem) {
                    return oldItem.equals(newItem);
                }
            };
    private LayoutInflater layoutInflater;

    public RecyclerLightAdapter() {
        super(DIFF_CALLBACK);
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public LightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        final LightRecyclerviewBinding binding =
                LightRecyclerviewBinding.inflate(layoutInflater, parent, false);

        return new LightViewHolder(binding);
    }

    private PublishSubject<Integer> mProgress = PublishSubject.create();
    private PublishSubject<Long> mId = PublishSubject.create();

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull LightViewHolder holder, int position) {

        Devices devices = getItem(position);
        holder.bind(devices);

        RxIOSStyleSlider.progressChanged(holder.binding.brightness)
                .subscribe(progress -> {
                    if(progress != holder.binding.getDevice().getBrightness()) {
                        holder.binding.getDevice().setBrightness(progress);
                        holder.binding.brightness.setText(holder.binding.getDevice().getBrightness()+ "%");
                        mProgress.onNext(progress);
                        mId.onNext(holder.binding.getDevice().getId());
                    }
                });
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    public Observable<ProgressId> getProgressObserver() {
        return Observable.zip(mProgress, mId, ProgressId::new);
    }

    public class ProgressId {
        public long id;
        public int progress;

        ProgressId(int progress, long id) {
            this.id = id;
            this.progress = progress;
        }
    }
}
