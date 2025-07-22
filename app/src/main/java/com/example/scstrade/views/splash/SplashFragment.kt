package com.example.scstrade.views.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.scstrade.databinding.FragmentSplashBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.login.LoginFragment
import com.example.scstrade.views.main.MainActivity
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : Fragment() {
    private var hasLoggedStartupTime = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (!hasLoggedStartupTime) {
                    hasLoggedStartupTime = true
                    view.viewTreeObserver.removeOnPreDrawListener(this)
                    val now = System.currentTimeMillis()
                    val duration = now - MyApp.appStartTime
                    Log.d("StartupTime", "App cold start completed in $duration ms")
                }
                return true
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSplashBinding.inflate(inflater, container, false)

        binding.main.postDelayed({
            val centerX = (binding.imageView.width - binding.imageViewLogo.width) / 2f
            val centerY = (binding.imageView.height - binding.imageViewLogo.height) / 2.5f
            val currentX = binding.imageViewLogo.x
            val currentY = binding.imageViewLogo.y
            val deltaX = centerX - currentX
            val deltaY = centerY - currentY

            val rootHeight = binding.root.height
            val startY = rootHeight.toFloat() + binding.bottomProgess.height

            binding.imageViewLogo.alpha = 0f
            binding.scsTradeP.alpha = 0f
            binding.bottomProgess.alpha = 0f
            binding.bottomProgess.translationY = startY
            binding.bottomProgess.visibility = View.VISIBLE

            val smallestWidth = Utils.getSmallestWidthDp(requireContext())
            val bottomTranslationY = if (smallestWidth in 321..399) -700f else -600f

            val animationSet = AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(binding.imageViewLogo, "translationX", 0f, deltaX),
                    ObjectAnimator.ofFloat(binding.imageViewLogo, "translationY", 0f, deltaY),
                    ObjectAnimator.ofFloat(binding.imageViewLogo, "alpha", 0f, 1f),
                    ObjectAnimator.ofFloat(binding.scsTradeP, "translationX", 0f, deltaX),
                    ObjectAnimator.ofFloat(binding.scsTradeP, "translationY", 0f, deltaY),
                    ObjectAnimator.ofFloat(binding.scsTradeP, "alpha", 0f, 1f),
                    ObjectAnimator.ofFloat(binding.bottomProgess, "translationX", 0f, deltaX),
                    ObjectAnimator.ofFloat(binding.bottomProgess, "translationY", 0f, bottomTranslationY),
                    ObjectAnimator.ofFloat(binding.bottomProgess, "alpha", 0f, 1f)
                )
                duration = 1000L
                interpolator = AccelerateDecelerateInterpolator()
            }

            animationSet.start()

            lifecycleScope.launch {
                delay(1000L) // wait for animation to complete

                ObjectAnimator.ofInt(binding.progressBar, "progress", 0, 100).apply {
                    duration = 2000L
                    interpolator = DecelerateInterpolator()
                    start()
                }

                delay(2000L) // wait for progress animation to complete

                // ✅ Moved SharedPreferences access to background thread
                val remembered = withContext(Dispatchers.IO) {
                    Utils.getSharedPreference(
                        requireContext(),
                        listOf(false),
                        AppConstants.IS_REMEMBER,
                        object : TypeToken<List<Boolean>>() {}
                    ).first()
                }

                // ✅ Navigation must run on Main thread
                val activity = requireActivity() as MainActivity
                if (remembered) {
                    activity.loadFragment(LandingFragment())
                } else {
                    activity.loadFragment(LoginFragment())
                }
            }
        }, 200)

        return binding.root
    }
}
