package com.google.zxing.client.android;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actein.qr.QrCodeProcessingResult;
import com.actein.qr.QrCodeStatus;
import com.actein.scanner.R;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.client.android.clipboard.ClipboardInterface;
import com.google.zxing.client.android.result.ResultButtonListener;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.android.result.supplement.SupplementalInfoRetriever;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;

class DecodeResultViewer
{
    DecodeResultViewer(CaptureActivity activity, ViewfinderView viewfinderView, TextView statusView, View resultView)
    {
        mActivity = activity;
        mViewfinderView = viewfinderView;
        mStatusView = statusView;
        mResultView = resultView;
    }

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Visibility {}

    private void buildStatusView(@Visibility int visibility)
    {
        mStatusView.setVisibility(visibility);
    }

    private void buildViewfinderView(@Visibility int visibility)
    {
        mViewfinderView.setVisibility(visibility);
    }

    private void buildResultView(@Visibility int visibility)
    {
        mResultView.setVisibility(visibility);
    }

    private void buildBarcodeView(QrCodeProcessingResult result, Bitmap barcode, boolean internal)
    {
        ImageView barcodeImageView = (ImageView) mActivity.findViewById(R.id.barcode_image_view);

        if (internal)
        {
            if (barcode == null)
            {
                barcodeImageView.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(),
                                                                             R.mipmap.ic_launcher_gradient));
            }
            else
            {
                barcodeImageView.setImageBitmap(barcode);
            }
        }
        else
        {
            int resourceId = R.drawable.qr_scanning_fail;
            if (result.getStatus() == QrCodeStatus.SUCCESS)
            {
                resourceId = R.drawable.qr_scanning_success;

            }
            barcodeImageView.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(),
                                                                         resourceId));
        }
    }

    private void buildFormatView(@Visibility int visibility, Result rawResult)
    {
        TextView formatTextView = (TextView) mActivity.findViewById(R.id.format_text_view);
        View formatTextViewLabel = mActivity.findViewById(R.id.format_text_view_label);
        formatTextView.setVisibility(visibility);
        formatTextViewLabel.setVisibility(visibility);
        if (visibility == View.VISIBLE)
        {
            formatTextView.setText(rawResult.getBarcodeFormat().toString());
        }
    }

    private void buildTypeView(@Visibility int visibility, ResultHandler resultHandler)
    {
        TextView typeTextView = (TextView) mActivity.findViewById(R.id.type_text_view);
        View typeTextViewLabel = mActivity.findViewById(R.id.type_text_view_label);
        typeTextView.setVisibility(visibility);
        typeTextViewLabel.setVisibility(visibility);
        if (visibility == View.VISIBLE)
        {
            typeTextView.setText(resultHandler.getType().toString());
        }
    }

    private void buildTimeView(@Visibility int visibility, Result rawResult)
    {
        TextView timeTextView = (TextView) mActivity.findViewById(R.id.time_text_view);
        View timeTextViewLabel = mActivity.findViewById(R.id.time_text_view_label);
        timeTextView.setVisibility(visibility);
        timeTextViewLabel.setVisibility(visibility);
        if (visibility == View.VISIBLE)
        {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
            timeTextView.setText(formatter.format(new Date(rawResult.getTimestamp())));
        }
    }

    private void buildMetaView(@Visibility int visibility, Result rawResult)
    {
        TextView metaTextView = (TextView) mActivity.findViewById(R.id.meta_text_view);
        View metaTextViewLabel = mActivity.findViewById(R.id.meta_text_view_label);
        metaTextView.setVisibility(visibility);
        metaTextViewLabel.setVisibility(visibility);

        if (visibility == View.VISIBLE)
        {
            Map<ResultMetadataType, Object> metadata = rawResult.getResultMetadata();
            if (metadata == null)
            {
                metaTextView.setVisibility(View.GONE);
                metaTextViewLabel.setVisibility(View.GONE);
            }
            else
            {
                StringBuilder metadataText = new StringBuilder(20);
                for (Map.Entry<ResultMetadataType, Object> entry : metadata.entrySet())
                {
                    if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey()))
                    {
                        metadataText.append(entry.getValue()).append('\n');
                    }
                }
                if (metadataText.length() > 0)
                {
                    metadataText.setLength(metadataText.length() - 1);
                    metaTextView.setText(metadataText.toString());
                }
            }
        }
    }

    private void buildContentsView(@Visibility int visibility, ResultHandler resultHandler)
    {
        TextView contentsTextView = (TextView) mActivity.findViewById(R.id.contents_text_view);
        contentsTextView.setVisibility(visibility);
        if (visibility == View.VISIBLE)
        {
            CharSequence displayContents = resultHandler.getDisplayContents();
            contentsTextView.setText(displayContents);
            int scaledSize = Math.max(22, 32 - displayContents.length() / 4);
            contentsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);
        }
    }

    private void buildResultTextView(@Visibility int visibility, String text)
    {
        TextView qrResultTextView = (TextView) mActivity.findViewById(R.id.qr_result_text_view);
        qrResultTextView.setVisibility(visibility);
        if (visibility == View.VISIBLE)
        {
            qrResultTextView.setText(text);
        }
    }

    private void buildSupplementTextView(@Visibility int visibility, ResultHandler resultHandler)
    {
        TextView supplementTextView = (TextView) mActivity.findViewById(R.id.contents_supplement_text_view);
        supplementTextView.setVisibility(visibility);
        if (visibility == View.VISIBLE)
        {
            supplementTextView.setText("");
            supplementTextView.setOnClickListener(null);
            if (PreferenceManager.getDefaultSharedPreferences(mActivity)
                                 .getBoolean(PreferencesActivity.KEY_SUPPLEMENTAL, true))
            {
                SupplementalInfoRetriever.maybeInvokeRetrieval(supplementTextView,
                                                               resultHandler.getResult(),
                                                               mActivity.getHistoryManager(),
                                                               mActivity
                                                               );
            }
        }
    }

    private void buildResultButtonView(@Visibility int visibility, ResultHandler resultHandler)
    {
        ViewGroup buttonView = (ViewGroup) mActivity.findViewById(R.id.result_button_view);
        buttonView.setVisibility(visibility);

        if (visibility == View.VISIBLE)
        {
            int buttonCount = resultHandler.getButtonCount();
            buttonView.requestFocus();
            for (int x = 0; x < ResultHandler.MAX_BUTTON_COUNT; x++)
            {
                TextView button = (TextView) buttonView.getChildAt(x);
                if (x < buttonCount)
                {
                    button.setVisibility(View.VISIBLE);
                    button.setText(resultHandler.getButtonText(x));
                    button.setOnClickListener(new ResultButtonListener(resultHandler, x));
                }
                else
                {
                    button.setVisibility(View.GONE);
                }
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    private void buildQrCodeStatusView(QrCodeProcessingResult result,
                                       ResultHandler resultHandler,
                                       boolean internal)
    {
        String message;
        switch (result.getStatus())
        {
        case QR_CODE_NOT_STARTED_YET:
            message = mActivity.getString(R.string.alert_dialog_qr_code_not_started_yet);
            break;
        case QR_CODE_EXPIRED:
            message = mActivity.getString(R.string.alert_dialog_qr_code_expired);
            break;
        case WRONG_LOCATION:
            message = mActivity.getString(R.string.alert_dialog_qr_code_wrong_location,
                                          result.getParsedResult().getInnerCalendarResult().getLocation());
            break;
        case WRONG_BOOTH:
            message = mActivity.getString(R.string.alert_dialog_qr_code_wrong_booth,
                                          result.getParsedResult().getBoothId());
            break;
        case DIGITAL_SIGNATURE_INVALID:
            message = mActivity.getString(R.string.alert_dialog_qr_code_digital_sign);
            break;
        case SUCCESS:
            message = mActivity.getString(R.string.alert_dialog_qr_code_success,
                                          result.getParsedResult().getEquipment());
            break;
        case QR_CODE_INVALID:
        default:
            message = mActivity.getString(R.string.alert_dialog_qr_code_invalid);
        }

        buildResultTextView(View.VISIBLE, message);
        if (internal)
        {
            buildContentsView(View.VISIBLE, resultHandler);
        }
        else
        {
            buildContentsView(View.GONE, resultHandler);
        }
    }

    private void copyToClipBoard(ResultHandler resultHandler)
    {
        if (mActivity.getCopyToClipBoard() && !resultHandler.areContentsSecure())
        {
            ClipboardInterface.setText(resultHandler.getDisplayContents(), mActivity);
        }
    }

    private void setViewDuration(long showDuration)
    {
        final Handler alertDismissHandler = new Handler();
        alertDismissHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mActivity.restartPreviewAfterDelay(0L);
            }
        }, showDuration);
    }

    // handle decode result with additional information
    void handleDecodeResultInternally(
            QrCodeProcessingResult result,
            Result rawResult,
            ResultHandler resultHandler,
            Bitmap barcode
            )
    {
        buildStatusView(View.GONE);
        buildViewfinderView(View.GONE);
        buildResultView(View.VISIBLE);

        buildBarcodeView(result, barcode, true);

        buildFormatView(View.VISIBLE, rawResult);
        buildTypeView(View.VISIBLE, resultHandler);
        buildTimeView(View.VISIBLE, rawResult);
        buildMetaView(View.VISIBLE, rawResult);

        buildResultTextView(View.GONE, "");
        buildContentsView(View.VISIBLE, resultHandler);

        buildQrCodeStatusView(result, resultHandler, true);

        buildSupplementTextView(View.VISIBLE, resultHandler);
        buildResultButtonView(View.VISIBLE, resultHandler);

        copyToClipBoard(resultHandler);
    }

    // handle decode result normally
    void handleDecodeResultExternally(
            QrCodeProcessingResult result,
            Result rawResult,
            ResultHandler resultHandler,
            Bitmap barcode
            )
    {
        buildStatusView(View.GONE);
        buildViewfinderView(View.GONE);
        buildResultView(View.VISIBLE);

        buildBarcodeView(result, barcode, false);

        buildFormatView(View.GONE, rawResult);
        buildTypeView(View.GONE, resultHandler);
        buildTimeView(View.VISIBLE, rawResult);
        buildMetaView(View.GONE, rawResult);

        buildQrCodeStatusView(result, resultHandler, false);

        buildSupplementTextView(View.GONE, resultHandler);
        buildResultButtonView(View.GONE, resultHandler);

        copyToClipBoard(resultHandler);
        long showDuration = 20000;
        setViewDuration(showDuration);
    }

    private CaptureActivity mActivity;
    private ViewfinderView mViewfinderView;
    private TextView mStatusView;
    private View mResultView;

    private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES =
            EnumSet.of(ResultMetadataType.ISSUE_NUMBER,
                       ResultMetadataType.SUGGESTED_PRICE,
                       ResultMetadataType.ERROR_CORRECTION_LEVEL,
                       ResultMetadataType.POSSIBLE_COUNTRY);
}
