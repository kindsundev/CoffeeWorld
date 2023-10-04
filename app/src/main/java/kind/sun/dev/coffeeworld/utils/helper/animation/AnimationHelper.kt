package kind.sun.dev.coffeeworld.utils.helper.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Interpolator
import kotlin.math.sin

private val cycleInterpolator = object : Interpolator {
    private val cycles = 0.5f

    override fun getInterpolation(input: Float): Float {
        return sin(2.0f * cycles * Math.PI * input).toFloat()
    }
}

/*
* The purpose of the effect is to click on an item.
* It will zoom in and out like the feeling of clicking on an object.
* */
fun View.setScaleAnimation(
    animDuration: Long,
    animScale: Float,
    onAnimationDone: () -> Unit
) {
    val scaleXAnimator = ObjectAnimator.ofFloat(this, View.SCALE_X, animScale).apply {
        interpolator = cycleInterpolator
    }
    val scaleYAnimator = ObjectAnimator.ofFloat(this, View.SCALE_Y, animScale).apply {
        interpolator = cycleInterpolator
    }
    AnimatorSet().apply {
        playTogether(scaleXAnimator, scaleYAnimator)
        duration = animDuration
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                onAnimationDone.invoke()
            }
        })
    }.start()
}

