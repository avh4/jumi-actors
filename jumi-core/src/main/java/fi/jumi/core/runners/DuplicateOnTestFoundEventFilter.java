// Copyright © 2011-2012, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package fi.jumi.core.runners;

import fi.jumi.api.drivers.TestId;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;

@NotThreadSafe
public class DuplicateOnTestFoundEventFilter implements TestClassListener {

    private final TestClassListener target;
    private final Map<TestId, String> tests = new HashMap<TestId, String>();

    public DuplicateOnTestFoundEventFilter(TestClassListener target) {
        this.target = target;
    }

    @Override
    public void onTestFound(TestId id, String name) {
        if (hasNotBeenFoundBefore(id)) {
            checkParentWasFoundFirst(id);
            rememberFoundTest(id, name);
            target.onTestFound(id, name);
        } else {
            checkNameIsSameAsBefore(id, name);
        }
    }

    private void rememberFoundTest(TestId id, String name) {
        tests.put(id, name);
    }

    private boolean hasNotBeenFoundBefore(TestId id) {
        return !tests.containsKey(id);
    }

    private void checkParentWasFoundFirst(TestId id) {
        if (!id.isRoot() && hasNotBeenFoundBefore(id.getParent())) {
            throw new IllegalStateException("parent of " + id + " must be found first");
        }
    }

    private void checkNameIsSameAsBefore(TestId id, String newName) {
        String oldName = tests.get(id);
        if (oldName != null && !oldName.equals(newName)) {
            throw new IllegalArgumentException("test " + id + " was already found with another name: " + oldName);
        }
    }

    // events which are delegated as-is

    @Override
    public void onTestStarted(TestId id) {
        target.onTestStarted(id);
    }

    @Override
    public void onFailure(TestId id, Throwable cause) {
        target.onFailure(id, cause);
    }

    @Override
    public void onTestFinished(TestId id) {
        target.onTestFinished(id);
    }
}