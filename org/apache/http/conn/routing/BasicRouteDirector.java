package org.apache.http.conn.routing;

import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/routing/BasicRouteDirector.class */
public class BasicRouteDirector implements HttpRouteDirector {
    @Override // org.apache.http.conn.routing.HttpRouteDirector
    public int nextStep(RouteInfo plan, RouteInfo fact) {
        int step;
        Args.notNull(plan, "Planned route");
        if (fact == null || fact.getHopCount() < 1) {
            step = firstStep(plan);
        } else if (plan.getHopCount() > 1) {
            step = proxiedStep(plan, fact);
        } else {
            step = directStep(plan, fact);
        }
        return step;
    }

    protected int firstStep(RouteInfo plan) {
        return plan.getHopCount() > 1 ? 2 : 1;
    }

    protected int directStep(RouteInfo plan, RouteInfo fact) {
        if (fact.getHopCount() > 1 || !plan.getTargetHost().equals(fact.getTargetHost()) || plan.isSecure() != fact.isSecure()) {
            return -1;
        }
        if (plan.getLocalAddress() != null && !plan.getLocalAddress().equals(fact.getLocalAddress())) {
            return -1;
        }
        return 0;
    }

    protected int proxiedStep(RouteInfo plan, RouteInfo fact) {
        int phc;
        int fhc;
        if (fact.getHopCount() <= 1 || !plan.getTargetHost().equals(fact.getTargetHost()) || (phc = plan.getHopCount()) < (fhc = fact.getHopCount())) {
            return -1;
        }
        for (int i = 0; i < fhc - 1; i++) {
            if (!plan.getHopTarget(i).equals(fact.getHopTarget(i))) {
                return -1;
            }
        }
        if (phc > fhc) {
            return 4;
        }
        if (!fact.isTunnelled() || plan.isTunnelled()) {
            if (fact.isLayered() && !plan.isLayered()) {
                return -1;
            }
            if (plan.isTunnelled() && !fact.isTunnelled()) {
                return 3;
            }
            if (plan.isLayered() && !fact.isLayered()) {
                return 5;
            }
            if (plan.isSecure() != fact.isSecure()) {
                return -1;
            }
            return 0;
        }
        return -1;
    }
}
