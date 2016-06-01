package coolthings.joey.grant.bulldozerxtreme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TickerTimer {

    public static final long DEFAULT_PERIOD = 100;
    private static final int MAX_TICK = 100000;
    private Timer timer;
    private long period = 30;
    private List<TickerTimerListener> listeners;
    private Map<String, Boolean> events;
    private int tick = 0;
    private boolean isRunning;

    public interface TickerTimerListener {
        void onTimerTick(int tick, long period);
    }

    public TickerTimer() {
        listeners = new ArrayList<TickerTimerListener>();
        events = new HashMap<>();

    }

    public void addEvent(String eventName) {
        events.put(eventName, false);
    }

    public void onEvent(String eventName) {
        events.put(eventName, true);
    }

    synchronized public TickerTimer start() {
        start(DEFAULT_PERIOD);
        return this;
    }

    synchronized public TickerTimer start(long period) {
        start(period, new Timer());
        return this;
    }

    synchronized TickerTimer start(long period, Timer timer) {
        stop();
        this.period = period;
        this.timer = timer;
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                onTick();
            }
        }, 0, period);
        isRunning = true;
        return this;
    }

    synchronized public TickerTimer stop() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
        timer = null;
        isRunning = false;
        return this;
    }

    synchronized private void onTick() {
        tick++;
        if (tick > MAX_TICK) {
            tick = 0;
        }
        try {
            for (TickerTimerListener listener : listeners) {
                listener.onTimerTick(tick, period);
            }
        } catch (Exception e) {
        }
    }

    public void clearListeners() {
        listeners.clear();
    }

    public boolean isRunning() {
        return isRunning;
    }

    synchronized public void unregisterListener(TickerTimerListener listener) {
        try {
            listeners.remove(listener);
        } catch (Exception e) {
        }
    }

    synchronized public void registerNewListener(TickerTimerListener listener) {
        listeners.add(listener);
    }

    synchronized Map<String, Boolean> getEvents() {
        return events;
    }

}
