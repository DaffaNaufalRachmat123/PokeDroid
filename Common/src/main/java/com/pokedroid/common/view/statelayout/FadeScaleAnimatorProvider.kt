package com.pokedroid.common.view.statelayout

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

class FadeScaleAnimatorProvider : AnimatorProvider {

    override fun showAnimation(view: View): Animator {
        val set = AnimatorSet()
        val object1: ObjectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        set.playTogether(object1)
        return set
    }

    override fun hideAnimation(view: View): Animator {
        val set = AnimatorSet()
        val object1: ObjectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        set.playTogether(object1)
        return set
    }
}