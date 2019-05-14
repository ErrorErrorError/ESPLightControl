package com.errorerrorerror.esplightcontrol.di.component;

import android.app.Application;

import com.errorerrorerror.esplightcontrol.di.AppModule;
import com.errorerrorerror.esplightcontrol.di.RoomModule;
import com.errorerrorerror.esplightcontrol.views.DialogFragment;
import com.errorerrorerror.esplightcontrol.views.HomeFragment;
import com.errorerrorerror.esplightcontrol.views.LightFragment;
import com.errorerrorerror.esplightcontrol.views.ModesFragment;
import com.errorerrorerror.esplightcontrol.views.MusicBottomSheetDialogFragment;
import com.errorerrorerror.esplightcontrol.views.PresetsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RoomModule.class})
public interface AppComponent {


    void inject(HomeFragment homeFragment);
    void inject(LightFragment lightFragment);
    void inject(ModesFragment modesFragment);
    void inject(PresetsFragment presetsFragment);
    void inject(DialogFragment dialogFragment);
    void inject(MusicBottomSheetDialogFragment musicBottomSheetDialogFragment);

    Application application();
}

