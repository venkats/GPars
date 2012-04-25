// GPars - Groovy Parallel Systems
//
// Copyright © 2008-11  The original author or authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package groovyx.gpars;

import groovyx.gpars.scheduler.Pool;
import groovyx.gpars.scheduler.ResizeablePool;
import groovyx.gpars.util.GeneralTimer;
import groovyx.gpars.util.PoolFactory;
import groovyx.gpars.util.TimerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Enables to specify custom thread pools and timers to run GPars in hosted environments, such as GAE
 *
 * @author Vaclav Pech
 */
public final class GParsConfig {
    private static volatile PoolFactory poolFactory;
    private static volatile TimerFactory timerFactory;

    public static void setPoolFactory(final PoolFactory pool) {
        if (pool == null) throw new IllegalArgumentException("The default pool must not be null");
        poolFactory = pool;
    }

    public static PoolFactory getPoolFactory() {
        return poolFactory;
    }

    /**
     * If a pool factory has been set, it will be used to create a new thread pool.
     * Otherwise a new instance of ResizeablePool will be returned.
     *
     * @return A thread pool instance to use for default parallel groups (actors, dataflow)
     */
    public static Pool retrieveDefaultPool() {
        if (poolFactory != null) return poolFactory.createPool();
        return new ResizeablePool(true, 1);
    }

    public static void setTimerFactory(final TimerFactory timerFactory) {
        if (timerFactory == null) throw new IllegalArgumentException("The TimerFactory must not be null");
        GParsConfig.timerFactory = timerFactory;
    }

    public static TimerFactory getTimerFactory() {
        return timerFactory;
    }

    public static GeneralTimer retrieveDefaultTimer(final String name, final boolean daemon) {
        if (timerFactory != null) return timerFactory.createTimer(name, daemon);
        return new GeneralTimer() {
            private final Timer timer = new Timer(name, daemon);

            @Override
            public void schedule(final TimerTask task, final long timeout) {
                timer.schedule(task, timeout);
            }
        };
    }
}