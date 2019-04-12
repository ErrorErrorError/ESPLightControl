package com.tenclouds.swipeablerecyclerviewcell.metaball

import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tenclouds.swipeablerecyclerviewcell.R
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.SwipeRevealLayout
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.interfaces.AnimatedRevealView
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.interfaces.OnDeleteListener
import com.tenclouds.swipeablerecyclerviewcell.utils.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.properties.Delegates

const val LEFT_VIEW_TO_DELETE = 1
const val RIGHT_VIEW_TO_DELETE = 2
const val NONE_VIEW_TO_DELETE = 0
const val TAG = "SwipeRevealLayout"

internal class MetaBalls : LinearLayout, AnimatedRevealView {
    private val calculatedSelectorRadius by lazy {
        max(rightView.width, rightView.height).div(2f)
    }

    private var destinationPoint: Point = Point()
    private var originPoint: Point = Point()

    private var startingOriginX: Float = 0f
    private var startingRevealedParentX: Float = 0f

    private lateinit var rightView: ImageView
    private lateinit var leftView: ImageView

    private var transitionDistance = 0.0f
    private var leftCircle = Circle()
    private var rightCircle = Circle()

    private val maxViewScale = 1.2f

    var deleteView = NONE_VIEW_TO_DELETE


    var rightViewColor: Int by Delegates.observable(
            ContextCompat.getColor(context, R.color.redDelete))
    { _, _, new -> rightCircle.paint.color = new }

    var leftViewColor: Int by Delegates.observable(
            ContextCompat.getColor(context, R.color.greyFavourite))
    { _, _, new -> leftCircle.paint.color = new }
    /*
        var connectorColor: Int by Delegates.observable(
                ContextCompat.getColor(context, R.color.redDelete))
        { _, _, new -> connectorPaint.color = new }
    */
    var leftIconResId: Int by Delegates.observable(
            R.drawable.ic_fav)
    { _, _, new -> leftView.setImageResource(new) }

    var rightIconResId: Int by Delegates.observable(
            R.drawable.ic_delete)
    { _, _, new -> rightView.setImageResource(new) }

    private var movementProgress = 0f
        set(value) {
            val startWhenProgress = 0.6f
            val diffRangeLimitOne = 1 - startWhenProgress
            field =
                    if (value in 0.0f..startWhenProgress) 0f
                    else (value - startWhenProgress) * 1.div(diffRangeLimitOne)
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        gravity = Gravity.CENTER
        orientation = LinearLayout.HORIZONTAL

        addRevealedViews()
    }

    fun configureIconsView(iconsMarginStart: Int,
                           iconsMarginEnd: Int,
                           iconsDistance: Int,
                           iconsSize: Int,
                           iconsPadding: Int) {

        val minMargin = iconsSize * maxViewScale - iconsSize
        val startMargin = max(minMargin.toInt(), iconsMarginStart)
        val endMargin = max(minMargin.toInt(), iconsMarginEnd)

        val leftLp = LinearLayout.LayoutParams(iconsSize, iconsSize)
                .apply { setMargins(startMargin, 0, iconsDistance / 2, 0) }

        val rightLp = LinearLayout.LayoutParams(iconsSize, iconsSize)
                .apply { setMargins(iconsDistance / 2, 0, endMargin, 0) }


        rightView.apply {
            layoutParams = rightLp
            padding(iconsPadding)
            id = generateViewId()
        }

        leftView.apply {
            layoutParams = leftLp
            padding(iconsPadding)
            id = generateViewId()
        }
    }

    private fun addRevealedViews() {
        rightView = ImageView(context)
        leftView = ImageView(context)

        addView(leftView)
        addView(rightView)

        configureClickListeners()
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        post {
            initValues()
            reveal(1f)
        }
        super.onLayout(changed, l, t, r, b)
    }

    private fun initValues() {
        originPoint = rightView.getCenter()
        destinationPoint = leftView.getCenter()
        transitionDistance = destinationPoint.x - originPoint.x
        startingOriginX = originPoint.x
        startingRevealedParentX = 0f
    }

    private fun clickAnimator(circle: Circle, viewColor: Int, icon: View) =
            valueAnimatorOfFloat(1f, maxViewScale, 1f,
                    updateListener = {
                        circle.radius = calculatedSelectorRadius * it
                        circle.paint.color = viewColor.blend(Color.WHITE, it - 1f)
                        calculateViewScale(it, icon)
                        invalidate()
                    }).setDuration(300)

    private fun deleteAnimator() =
            valueAnimatorOfFloat(0f, 1f,
                    updateListener = {
                        calculateValuesForDeleteAnimation(it, startingOriginX)
                        invalidate()
                    },
                    onAnimEnd = { resetViewState() },
                    onAnimStart = { (parent as? OnDeleteListener)?.deleted() })
                    .setDuration(300)


    private fun configureClickListeners() {
        rightView.setOnClickListener {
            var delay = 0L
            if (deleteView == RIGHT_VIEW_TO_DELETE) {
                deleteAnimation(rightCircle, rightViewColor, rightView)
                delay = 300L
            } else {
                clickAnimation(rightCircle, rightViewColor, rightView)
            }
            postDelayed({
                (parent as? SwipeRevealLayout)?.onIconClickListener?.onRightIconClick()
            }, delay)
        }

        leftView.setOnClickListener {
            var delay = 0L
            if (deleteView == LEFT_VIEW_TO_DELETE) {
                deleteAnimation(leftCircle, leftViewColor, leftView)
                delay = 300L
            } else {
                clickAnimation(leftCircle, leftViewColor, leftView)
            }
            postDelayed({
                (parent as? SwipeRevealLayout)?.onIconClickListener?.onLeftIconClick()
            }, delay)
        }
    }

    private fun resetViewState() {
        originPoint.x = startingOriginX
        calculateViewPosition(rightView, originPoint)
    }

    private fun deleteAnimation(circle: Circle, color: Int, icon: View) {
        AnimatorSet()
                .apply {
                    playSequentially(
                            clickAnimator(circle, color, icon),
                            deleteAnimator()
                    )
                }
                .start()
    }

    private fun clickAnimation(circle: Circle, color: Int, icon: View) {
        clickAnimator(circle, color, icon)
                .start()
    }

    override fun reveal(howMuchToReveal: Float) {
        movementProgress = howMuchToReveal
        calculateValuesDependingOnMovementProgress(movementProgress)

        invalidate()
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.drawCircle(
                originPoint.x,
                originPoint.y,
                rightCircle.radius,
                rightCircle.paint
        )

        canvas.drawCircle(
                destinationPoint.x,
                destinationPoint.y,
                leftCircle.radius,
                leftCircle.paint
        )

        super.dispatchDraw(canvas)
    }

    private fun calculateValuesDependingOnMovementProgress(progress: Float) {
        leftCircle.radius = getRadiusDependingOnViewPosition(progress)
        rightCircle.radius = getRadiusDependingOnViewPosition(progress)

        destinationPoint.x = originPoint.x + transitionDistance * progress

        calculateViewPosition(leftView, destinationPoint)
        calculateViewAlpha(progress, rightView)
    }

    private fun calculateViewAlpha(progress: Float, view: View)
    {
        with(view)
        {
            alpha = progress
        }
    }

    private fun calculateValuesForDeleteAnimation(progress: Float, startingX: Float) {
        originPoint.x = startingX + transitionDistance * progress
        movementProgress = progress

        calculateViewPosition(rightView, originPoint)
    }

    private fun getRadiusDependingOnViewPosition(progress: Float): Float {
        val startWhenProgress = 0.0f //Used to be .6f

        return if (movementProgress < startWhenProgress) {
            //max 1.6f
            val scale = (1 + (abs(progress - startWhenProgress)))
            calculatedSelectorRadius * scale
        } else {
            calculatedSelectorRadius
        }
    }

    private fun calculateViewPosition(view: View, destination: Point) {
        with(view) {
            x = destination.x - measuredWidth / 2
            y = destination.y - measuredHeight / 2
        }
    }

    private fun calculateViewScale(progress: Float, view: View) {
        with(view) {
            scaleY = progress
            scaleX = progress
        }
    }
}
