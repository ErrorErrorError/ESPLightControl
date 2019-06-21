package com.errorerrorerror.esplightcontrol.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.text.Charsets;

public class ObservableSocket {

    private Socket socket;
    private long id;
    private Observable<Boolean> connection;
    private final int initialIntervalInMs;
    private final int intervalInMs;
    private String host;
    private int port;
    private final int timeoutInMs;
    private boolean running = true;
    private String data;
    private Disposable disposable;


    public ObservableSocket(long id,
                            final int initialIntervalInMs,
                            final int intervalInMs,
                            String host,
                            int port,
                            final int timeoutInMs, String json,
                            boolean running) {
        this.id = id;
        this.initialIntervalInMs = initialIntervalInMs;
        this.intervalInMs = intervalInMs;
        this.timeoutInMs = timeoutInMs;
        this.host = host;
        this.port = port;
        this.data = json;
        this.running = running;
    }

    public Observable<Boolean> startObservableConnection() {
        return connection = Observable.interval(initialIntervalInMs, intervalInMs, TimeUnit.MILLISECONDS,
                Schedulers.io()).map(tick -> isConnected(host, port, timeoutInMs)).distinctUntilChanged().doOnSubscribe(disposable1 -> disposable = disposable1);
    }

    private boolean isConnected(String host, int port, final int timeoutInMs) {
        socket = new Socket();
        return isConnected(socket, host, port, timeoutInMs);
    }

    private boolean isConnected(Socket socket, String host, int port,
                                final int timeoutInMs) {
        boolean isConnected;
        try {
            socket.connect(new InetSocketAddress(host, port), timeoutInMs);
            isConnected = socket.isConnected();
            sendData(data);
            Log.d(Constants.HOME_TAG, data);
        } catch (IOException e) {
            isConnected = Boolean.FALSE;
        } finally {
            try {
                socket.close();
            } catch (IOException exception) {
                isConnected = Boolean.FALSE;
            }
        }
        return isConnected;
    }

    public Observable<Boolean> getConnectionObserver() {
        return connection.doOnSubscribe(disposable1 -> disposable = disposable1);
    }

    public long getId() {
        return id;
    }

    public Socket getSocket() {
        return socket;
    }

    private void sendData(String data) {
        OutputStreamWriter out;
        try {
            out = new OutputStreamWriter(socket.getOutputStream(), Charsets.UTF_8);
            out.write(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void stop() {
        this.running = false;
        disposable.dispose();
    }

    private static String toPrettyFormat(String jsonString)
    {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);
    }
}
