package life.chenshi.keepaccounts.module.common.utils

import android.animation.TimeInterpolator
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.transition.Transition
import androidx.transition.TransitionSet

inline fun transitionTogether(crossinline body: TransitionSet.() -> Unit): TransitionSet {
    return TransitionSet().apply {
        ordering = TransitionSet.ORDERING_TOGETHER
        body()
    }
}

operator fun TransitionSet.plusAssign(transition: Transition) {
    addTransition(transition)
}


val FAST_OUT_SLOW_IN: TimeInterpolator by lazy(LazyThreadSafetyMode.NONE) {
    PathInterpolatorCompat.create(0.4f, 0f, 0.2f, 1f)
}

/**
 * Decelerate easing.
 *
 * Incoming elements are animated using deceleration easing, which starts a transition at peak
 * velocity (the fastest point of an element’s movement) and ends at rest.
 */
val LINEAR_OUT_SLOW_IN: TimeInterpolator by lazy(LazyThreadSafetyMode.NONE) {
    PathInterpolatorCompat.create(0f, 0f, 0.2f, 1f)
}

/**
 * Accelerate easing.
 *
 * Elements exiting a screen use acceleration easing, where they start at rest and end at peak
 * velocity.
 */
val FAST_OUT_LINEAR_IN: TimeInterpolator by lazy(LazyThreadSafetyMode.NONE) {
    PathInterpolatorCompat.create(0.4f, 0f, 1f, 1f)
}
