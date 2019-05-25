package com.errorerrorerror.esplightcontrol.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;

import com.errorerrorerror.esplightcontrol.databinding.RecyclerviewLayoutBinding;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.views.ModesFragment;
import com.errorerrorerror.esplightcontrol.views.MusicBottomSheetDialogFragment;
import com.errorerrorerror.esplightcontrol.views.SolidBottomSheetDialogFragment;
import com.errorerrorerror.esplightcontrol.views.WavesBottomSheetDialogFragment;

public class ListModesAdapter extends DataBindingAdapter<Device> {
    private static final DiffUtil.ItemCallback<Device> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Device>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Device oldItem, @NonNull Device newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Device oldItem,
                                                  @NonNull Device newItem) {
                    return oldItem.equals(newItem) && oldItem.getOn().equals(newItem.getOn());
                }
            };

    private MusicBottomSheetDialogFragment music;
    private SolidBottomSheetDialogFragment solid;
    private WavesBottomSheetDialogFragment waves;
    private ModesFragment modesFragment;

    public ListModesAdapter(Fragment viewDataBinding) {
        super(DIFF_CALLBACK);
        setHasStableIds(true);
        if (viewDataBinding instanceof MusicBottomSheetDialogFragment) {
            this.music = (MusicBottomSheetDialogFragment) viewDataBinding;
        } else if (viewDataBinding instanceof SolidBottomSheetDialogFragment) {
            this.solid = (SolidBottomSheetDialogFragment) viewDataBinding;

        } else if (viewDataBinding instanceof WavesBottomSheetDialogFragment) {
            this.waves = (WavesBottomSheetDialogFragment) viewDataBinding;
        } else if ( viewDataBinding instanceof ModesFragment){
            this.modesFragment = (ModesFragment) viewDataBinding;
        }
    }

    @NonNull
    @Override
    public DeviceViewHolder<Device> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final RecyclerviewLayoutBinding binding =
                RecyclerviewLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        if (music != null) {
            binding.setViewM(music);
        } else if (waves != null) {
            binding.setViewW(waves);
        } else if (solid != null) {
            binding.setViewS(solid);
        } else if(modesFragment != null){
            binding.setViewModes(modesFragment);
        }

        return new DeviceViewHolder<>(binding);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }
}
