@file:JvmName("ActivityExtension")

package com.pangaeaedu.xhub.digitalscreen.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.pangaeaedu.xhub.digitalscreen.util.LifecycleUtil


fun FragmentActivity.findFragmentById(@IdRes id: Int): Fragment? {
    return supportFragmentManager.findFragmentById(id)
}

fun FragmentActivity.addFragment(@IdRes containerViewId: Int, fragment: Fragment) {
    LifecycleUtil.commitTransactionSafely(this, object: LifecycleUtil.TransactionBuilder {
        override fun createTransaction(transaction: FragmentTransaction): FragmentTransaction {
            return transaction.add(containerViewId, fragment)
        }
    })
}

fun FragmentActivity.replaceFragment(@IdRes containerViewId: Int, fragment: Fragment) {
    LifecycleUtil.commitTransactionSafely(this, object: LifecycleUtil.TransactionBuilder {
        override fun createTransaction(transaction: FragmentTransaction): FragmentTransaction {
            return transaction.replace(containerViewId, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
    })
}

fun FragmentActivity.replaceFragmentAddToBackStack(@IdRes containerViewId: Int, fragment: Fragment) {
    LifecycleUtil.commitTransactionSafely(this, object: LifecycleUtil.TransactionBuilder {
        override fun createTransaction(transaction: FragmentTransaction): FragmentTransaction {
            return transaction.replace(containerViewId, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
        }
    })
}

fun FragmentActivity.replaceFragmentClearBackStack(@IdRes containerViewId: Int, fragment: Fragment) {
    // Documentation says that passing null as the first parameter for popBackStack()
    // will only pop the top state. Diane hackborne says that it's wrong and should
    // pop the entire back stack.
    // https://groups.google.com/forum/#!topic/android-developers/0qXCA9rW7EI
    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    replaceFragment(containerViewId, fragment)
}
