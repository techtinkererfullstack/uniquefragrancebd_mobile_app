package com.example.uniquefragrancebd

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.uniquefragrancebd.databinding.ActivityMainBinding
import com.example.uniquefragrancebd.presentation.cart.CartViewModel
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply Material 3 dynamic colors if available (Android 12+)
        DynamicColors.applyToActivityIfAvailable(this)

        // Enable edge-to-edge before calling super
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle system bar insets for edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(left = insets.left, right = insets.right)
            windowInsets
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.productListFragment,
                R.id.wishlistFragment,
                R.id.cartFragment,
                R.id.orderListFragment,
                R.id.profileFragment,
                R.id.loginFragment,
                R.id.signupFragment
            )
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)

        // Hide BottomNavigationView on specific fragments
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment,
                R.id.productDetailFragment,
                R.id.checkoutFragment -> {
                    binding.bottomNav.visibility = View.GONE
                    binding.appBarLayout.visibility = if (destination.id == R.id.splashFragment) View.GONE else View.VISIBLE
                }
                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.appBarLayout.visibility = View.VISIBLE
                }
            }
        }

        observeCartBadge()
    }

    private fun observeCartBadge() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.state.collect { state ->
                    val itemCount = state.cartItems.sumOf { it.quantity }
                    val badge = binding.bottomNav.getOrCreateBadge(R.id.cartFragment)
                    if (itemCount > 0) {
                        badge.isVisible = true
                        badge.number = itemCount
                    } else {
                        badge.isVisible = false
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}