package com.android.libraries.transformationlayout

import android.app.Activity
import android.transition.Transition
import android.transition.TransitionManager
import android.util.ArrayMap
import android.view.ViewGroup
import java.lang.ref.WeakReference
import java.lang.reflect.Field
import java.util.*


object SharedElementFixHelper {

    // An ugly hack to prevent memory leaks in Transitions
    // https://stackoverflow.com/questions/32698049/sharedelement-and-custom-entertransition-causes-memory-leak/34542473#34542473
    fun removeActivityFromTransitionManager(activity: Activity) {
        val transitionManagerClass: Class<*> = TransitionManager::class.java
        try {
            val runningTransitionsField: Field =
                transitionManagerClass.getDeclaredField("sRunningTransitions")
            runningTransitionsField.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val runningTransitions: ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>?> =
                runningTransitionsField.get(transitionManagerClass) as ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>?>
            if (runningTransitions.get() == null || runningTransitions.get()?.get() == null) {
                return
            }
            val map: ArrayMap<ViewGroup, ArrayList<Transition>> =
                runningTransitions.get()?.get() as ArrayMap<ViewGroup, ArrayList<Transition>>
            map[activity.window.decorView]?.let { transitionList ->
                transitionList.forEach { transition ->
                    //Add a listener to all transitions. The last one to finish will remove the decor view:
                    transition.addListener(object : Transition.TransitionListener {
                        override fun onTransitionEnd(transition: Transition) {
                            //When a transition is finished, it gets removed from the transition list
                            // internally right before this callback. Remove the decor view only when
                            // all the transitions related to it are done:
                            if (transitionList.isEmpty()) {
                                map.remove(activity.window.decorView)
                            }
                            transition.removeListener(this)
                        }

                        override fun onTransitionCancel(transition: Transition?) {}
                        override fun onTransitionPause(transition: Transition?) {}
                        override fun onTransitionResume(transition: Transition?) {}
                        override fun onTransitionStart(transition: Transition?) {}
                    })
                }
                //If there are no active transitions, just remove the decor view immediately:
                if (transitionList.isEmpty()) {
                    map.remove(activity.window.decorView)
                }
            }
        } catch (_: Throwable) { }
    }
}