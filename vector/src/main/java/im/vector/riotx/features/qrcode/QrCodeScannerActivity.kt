/*
 * Copyright 2020 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.riotx.features.qrcode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import im.vector.riotx.R
import im.vector.riotx.core.di.ScreenComponent
import im.vector.riotx.core.extensions.replaceFragment
import im.vector.riotx.core.platform.VectorBaseActivity

class QrCodeScannerActivity : VectorBaseActivity() {

    override fun getLayoutRes() = R.layout.activity_simple

    override fun injectWith(injector: ScreenComponent) {
        injector.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isFirstCreation()) {
            replaceFragment(R.id.simpleFragmentContainer, QrCodeScannerFragment::class.java)
        }
    }

    fun setResultAndFinish(result: Result?) {
        result?.let {
            setResult(RESULT_OK, Intent().apply {
                putExtra(EXTRA_OUT_TEXT, it.text)
                putExtra(EXTRA_OUT_IS_QR_CODE, it.barcodeFormat == BarcodeFormat.QR_CODE)
            })
        }
        finish()
    }

    companion object {
        private const val EXTRA_OUT_TEXT = "EXTRA_OUT_TEXT"
        private const val EXTRA_OUT_IS_QR_CODE = "EXTRA_OUT_IS_QR_CODE"

        const val QR_CODE_SCANNER_REQUEST_CODE = 429

        // For test only
        fun startForResult(activity: Activity, requestCode: Int = QR_CODE_SCANNER_REQUEST_CODE) {
            activity.startActivityForResult(Intent(activity, QrCodeScannerActivity::class.java), requestCode)
        }

        fun startForResult(fragment: Fragment, requestCode: Int = QR_CODE_SCANNER_REQUEST_CODE) {
            fragment.startActivityForResult(Intent(fragment.requireActivity(), QrCodeScannerActivity::class.java), requestCode)
        }

        fun getResultText(data: Intent?): String? {
            return data?.getStringExtra(EXTRA_OUT_TEXT)
        }

        fun getResultIsQrCode(data: Intent?): Boolean {
            return data?.getBooleanExtra(EXTRA_OUT_IS_QR_CODE, false) == true
        }
    }
}