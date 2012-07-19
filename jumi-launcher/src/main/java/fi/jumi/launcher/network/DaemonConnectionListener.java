// Copyright © 2011-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package fi.jumi.launcher.network;

import fi.jumi.actors.ActorRef;
import fi.jumi.actors.eventizers.Event;
import fi.jumi.core.SuiteListener;

public interface DaemonConnectionListener {

    void onDaemonConnected(ActorRef<DaemonConnection> daemonConnection);

    void onMessageFromDaemon(Event<SuiteListener> message);
}
