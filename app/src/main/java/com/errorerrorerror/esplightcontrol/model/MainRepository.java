package com.errorerrorerror.esplightcontrol.model;

import com.errorerrorerror.esplightcontrol.model.device_music.MusicRepository;
import com.errorerrorerror.esplightcontrol.model.device_solid.SolidRepository;
import com.errorerrorerror.esplightcontrol.model.device_waves.WavesRepository;

import javax.inject.Inject;

public class MainRepository {
    private MusicRepository musicRepository;
    private SolidRepository solidRepository;
    private WavesRepository wavesRepository;

    @Inject
    public MainRepository(MusicRepository musicRepository, SolidRepository solidRepository, WavesRepository wavesRepository) {
        this.musicRepository = musicRepository;
        this.solidRepository = solidRepository;
        this.wavesRepository = wavesRepository;
    }

    public MusicRepository getMusicRepository() {
        return musicRepository;
    }

    public SolidRepository getSolidRepository() {
        return solidRepository;
    }

    public WavesRepository getWavesRepository() {
        return wavesRepository;
    }
}
