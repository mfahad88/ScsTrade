package com.example.scstrade.views.splash
import androidx.compose.ui.res.dimensionResource

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentSplashBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.login.LoginFragment
import com.example.scstrade.views.main.MainActivity
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding=FragmentSplashBinding.inflate(inflater,container,false)
      /*  ViewCompat.setOnApplyWindowInsetsListener(binding.bottomItem){ v, windowInsets->
            val insets= windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<MarginLayoutParams> {
                bottomMargin=insets.bottom
            }
            windowInsets
        }*/
        binding.main.post {
            val centerX = (binding.imageView.width - binding.imageViewLogo.width) / 2f
            val centerY = (binding.imageView.height - binding.imageViewLogo.height) / 2.5f

            // Get current absolute position of imageView relative to rootLayout
            val currentX = binding.imageViewLogo.x
            val currentY = binding.imageViewLogo.y



            val deltaX = centerX - currentX
            val deltaY = centerY - currentY

            val rootHeight = binding.root.height
            val targetY = rootHeight - binding.bottomProgess.height.toFloat() // bottom of root layout
            val startY = rootHeight.toFloat() + binding.bottomProgess.height

            binding.imageViewLogo.apply {
                alpha = 0f // start from invisible
            }
            binding.scsTradeP.apply {
                alpha = 0f
            }

            binding.bottomProgess.apply {
                alpha = 0f
                translationY = startY
                visibility = View.VISIBLE
            }

            AnimatorSet().apply {
                addListener(object:Animator.AnimatorListener{
                    override fun onAnimationStart(p0: Animator) {
                    }

                    override fun onAnimationEnd(p0: Animator) {
                        ObjectAnimator.ofInt(binding.progressBar, "progress", 0, 100)
                            .apply {
                                addListener(object : Animator.AnimatorListener{
                                override fun onAnimationStart(p0: Animator) {
                                }

                                override fun onAnimationEnd(p0: Animator) {
                                    if(Utils.getSharedPreference(requireContext(), listOf(false),AppConstants.IS_REMEMBER, object : TypeToken<List<Boolean>>() {}).first()){
                                        (requireActivity() as MainActivity).loadFragment(LandingFragment())
                                    }else{
                                        (requireActivity() as MainActivity).loadFragment(LoginFragment())
                                    }

                                }

                                override fun onAnimationCancel(p0: Animator) {
                                }

                                override fun onAnimationRepeat(p0: Animator) {
                                }

                            })
                            duration = 2000 // 2 seconds
                            interpolator = android.view.animation.DecelerateInterpolator()
                            start()
                        }
                    }

                    override fun onAnimationCancel(p0: Animator) {}

                    override fun onAnimationRepeat(p0: Animator) {
                    }

                })
                val smallestWidth = Utils.getSmallestWidthDp(requireContext())
                if(smallestWidth>320 && smallestWidth<400) {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.imageViewLogo, "translationX", 0f, deltaX),
                        ObjectAnimator.ofFloat(binding.imageViewLogo, "translationY", 0f, deltaY),
                        ObjectAnimator.ofFloat(binding.imageViewLogo, "alpha", 0f, 1f),
                        ObjectAnimator.ofFloat(binding.scsTradeP, "translationX", 0f, deltaX),
                        ObjectAnimator.ofFloat(binding.scsTradeP, "translationY", 0f, deltaY),
                        ObjectAnimator.ofFloat(binding.scsTradeP, "alpha", 0f, 1f),
                        ObjectAnimator.ofFloat(binding.bottomProgess, "translationX", 0f, deltaX),
                        ObjectAnimator.ofFloat(binding.bottomProgess, "translationY", 0f, -700f),
                        ObjectAnimator.ofFloat(binding.bottomProgess, "alpha", 0f, 1f)
                    )
                }else{
                    playTogether(
                        ObjectAnimator.ofFloat(binding.imageViewLogo, "translationX", 0f, deltaX),
                        ObjectAnimator.ofFloat(binding.imageViewLogo, "translationY", 0f, deltaY),
                        ObjectAnimator.ofFloat(binding.imageViewLogo, "alpha", 0f, 1f),
                        ObjectAnimator.ofFloat(binding.scsTradeP, "translationX", 0f, deltaX),
                        ObjectAnimator.ofFloat(binding.scsTradeP, "translationY", 0f, deltaY),
                        ObjectAnimator.ofFloat(binding.scsTradeP, "alpha", 0f, 1f),
                        ObjectAnimator.ofFloat(binding.bottomProgess, "translationX", 0f, deltaX),
                        ObjectAnimator.ofFloat(binding.bottomProgess, "translationY", 0f, -600f),
                        ObjectAnimator.ofFloat(binding.bottomProgess, "alpha", 0f, 1f)
                    )
                }
                this.duration = 1000L
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
        }
       /* lifecycleScope.launch {
            delay(5000)

            if(Utils.getSharedPreference(requireContext(), listOf(false),AppConstants.IS_REMEMBER, object : TypeToken<List<Boolean>>() {}).first()){
                (requireActivity() as MainActivity).loadFragment(LandingFragment())
            }else{
                (requireActivity() as MainActivity).loadFragment(LoginFragment())
            }

        }*/

        return  binding.root
    }

}