package com.errorerrorerror.esplightcontrol.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.errorerrorerror.esplightcontrol.databinding.RecyclerDevicesListBinding;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.rxobservable.RxSwipeRevealLayout;
import com.errorerrorerror.esplightcontrol.utils.Constants;
import com.jakewharton.rxbinding3.widget.RxCompoundButton;
import com.tenclouds.swipeablerecyclerviewcell.metaball.MetaBallsKt;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RecyclerDeviceAdapter extends ListAdapter<Devices, DevicesViewHolder> {

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
    private PublishSubject<Boolean> mSwitched = PublishSubject.create();
    private PublishSubject<Long> mPosition = PublishSubject.create();
    private PublishSubject<Devices> mDeviceDelete = PublishSubject.create();
    private PublishSubject<Long> mPositionEdit = PublishSubject.create();

    public RecyclerDeviceAdapter() {
        super(DIFF_CALLBACK);
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        final RecyclerDevicesListBinding binding =
                RecyclerDevicesListBinding.inflate(layoutInflater, parent, false);

        return new DevicesViewHolder(binding);
    }


    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull DevicesViewHolder holder, int position) {

        Devices device = getItem(position);
        holder.bind(device);

        RxCompoundButton.checkedChanges(holder.binding.connectionSwitch)
                .subscribe(aBoolean -> {
                    mSwitched.onNext(aBoolean);
                    mPosition.onNext(holder.getItemId());
                    Log.d(Constants.HOME_TAG, "onBindViewHolder: " + aBoolean + " " + holder.getItemId());
                }, onError -> Log.e(Constants.HOME_TAG, "onBindViewHolder: ", onError));

        /*holder.binding.connectionSwitch.setOnStateChangeListener((progress, state, jtb) ->
                onClickedSwitchListener.OnSwitched(jtb.isChecked(), holder.getAdapterPosition(), jtb));
        */

        RxSwipeRevealLayout.leftClickedIcon(holder.binding.swipeLayout, MetaBallsKt.NONE_VIEW_TO_DELETE)
                .subscribe(o -> {
                    mPositionEdit.onNext(holder.getItemId());
                    holder.binding.swipeLayout.close(true);
                    Log.d(Constants.HOME_TAG, "onBindViewHolder: " + holder.getItemId());
                }, onError -> Log.e(Constants.HOME_TAG, "onBindViewHolder: ", onError ));

        RxSwipeRevealLayout.rightClickedIcon(holder.binding.swipeLayout, MetaBallsKt.RIGHT_VIEW_TO_DELETE)
                .subscribe(o -> {
                            mDeviceDelete.onNext(getItem(holder.getAdapterPosition()));
                            Log.d(Constants.HOME_TAG, "onBindViewHolder: " + getItem(holder.getAdapterPosition()));
                }, onError -> Log.e(Constants.HOME_TAG, "onBindViewHolder: ", onError));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    protected Devices getItem(int position) {
        return getCurrentList().get(position);
    }


    public Observable<SwitchBoolInt> getListenerSwitch() {
        return Observable.zip(mSwitched, mPosition, SwitchBoolInt::new);
    }

    public Observable<Devices> getDeleteDeviceObservable() {
        return mDeviceDelete;
    }

    public Observable<Long> getEditDeviceObservable() {
        return mPositionEdit;
    }

    public class SwitchBoolInt {
        public Boolean bool;
        public Long id;

        SwitchBoolInt(Boolean bool, Long aLong) {
            this.bool = bool;
            this.id = aLong;
        }
    }
}
