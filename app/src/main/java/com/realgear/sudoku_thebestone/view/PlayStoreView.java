package com.realgear.sudoku_thebestone.view;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.activities.PlayStore;
import com.realgear.sudoku_thebestone.adapters.PlayStoreRecyclerViewAdapter;
import com.realgear.sudoku_thebestone.billing.BillingService;
import com.realgear.sudoku_thebestone.core.SkuItem;
import com.realgear.sudoku_thebestone.data.BillingServiceData;
import com.realgear.sudoku_thebestone.theme.Themeable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayStoreView extends Themeable {
    private static final String TAG = PlayStoreView.class.getSimpleName();


    private final PlayStore mActivity;

    private final BillingService        mBillingService;
    private final BillingServiceData    mBillingServiceData;

    private RecyclerView mProductsRecyclerView;
    private PlayStoreRecyclerViewAdapter mAdapter;

    public PlayStoreView(PlayStore activity) {
        this.mActivity = activity;


        mProductsRecyclerView = mActivity.findViewById(R.id.recyclerView_shop);
        mAdapter = new PlayStoreRecyclerViewAdapter(new ArrayList<>());

        mProductsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mProductsRecyclerView.setAdapter(mAdapter);

        this.mBillingServiceData = new BillingServiceData(mActivity);

        this.mBillingService = new BillingService(mActivity,
                getPurchaseListener(),
                getStateListener(),
                getSkuResponseListener(),
                getAcknowledgeResponseListener(),
                getPurchaseResponseListener());

        onConnect();

        FloatingActionButton backBtn = (FloatingActionButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
    }

    private void onConnect() {
        if(mBillingService != null) {
            mBillingService.startConnection();
        }
    }

    public void startPurchaseFlow(SkuDetails skuDetails) {
        if(mBillingService == null)
            return;

        if(!mBillingService.isReady())
            return;

        if(skuDetails == null)
            return;

        mBillingService.launchBillingFlow(skuDetails);
    }

    private PurchasesUpdatedListener getPurchaseListener() {
        return new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull @NotNull BillingResult billingResult, @Nullable @org.jetbrains.annotations.Nullable List<Purchase> list) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    for(Purchase purchase : list) {
                        if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                            handlePurchase(purchase);
                            mBillingService.handlePurchase(purchase);
                        }
                    }
                }

                switch (billingResult.getResponseCode()) {
                    case BillingClient.BillingResponseCode.OK:
                        Log.e(TAG, "Billing Response OK On Purchase Updated");
                        break;

                    case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                        Log.e(TAG, "Billing Response UNAVAILABLE On Purchase Updated");
                        break;


                    case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                        Log.e(TAG, "Billing Response DEVELOPER_ERROR On Purchase Updated");
                        break;


                    case BillingClient.BillingResponseCode.ERROR:
                        Log.e(TAG, "Billing Response ERROR On Purchase Updated");
                        break;


                    case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                        Log.e(TAG, "Billing Response FEATURE_NOT_SUPPORTED On Purchase Updated");
                        break;

                    case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                        Log.e(TAG, "Billing Response ITEM_ALREADY_OWNED On Purchase Updated");
                        break;

                    case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
                        Log.e(TAG, "Billing Response ITEM_NOT_OWNED On Purchase Updated");
                        break;

                    case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                        Log.e(TAG, "Billing Response ITEM_UNAVAILABLE On Purchase Updated");
                        break;

                    case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                        Log.e(TAG, "Billing Response SERVICE_DISCONNECTED On Purchase Updated");
                        break;

                    case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                        Log.e(TAG, "Billing Response SERVICE_TIMEOUT On Purchase Updated");
                        break;

                    case BillingClient.BillingResponseCode.USER_CANCELED:
                        Log.e(TAG, "Billing Response USER_CANCELED On Purchase Updated");
                        break;

                    case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
                        Log.e(TAG, "Billing Response SERVICE_UNAVAILABLE On Purchase Updated");
                        break;
                }
            }
        };
    }

    public void handlePurchase(Purchase purchase) {
        String skuId = purchase.getSkus().get(0);
        Log.e(TAG, "Sku ID : " + skuId);

        switch (skuId) {
            case BillingService.REMOVE_ADS:
                int index = mAdapter.setSkuPurchased(skuId, true);
                if(index >= 0) {
                    mBillingServiceData.setRemovedAds(true);
                    mProductsRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            mProductsRecyclerView.getAdapter().notifyItemChanged(index);
                        }
                    });
                }
                break;
        }
    }

    private BillingClientStateListener getStateListener() {
        return new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                Log.e(TAG, "Billing Service Is Disconnected");
            }

            @Override
            public void onBillingSetupFinished(@NonNull @NotNull BillingResult billingResult) {
                switch (billingResult.getResponseCode()) {
                    case BillingClient.BillingResponseCode.OK:
                        Log.e(TAG, "onBillingSetupFinished -> Billing Response OK");
                        break;

                    case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                        Log.e(TAG, "onBillingSetupFinished -> Billing Response UNAVAILABLE");
                        break;


                    case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                        Log.e(TAG, "onBillingSetupFinished -> Billing Response DEVELOPER_ERROR");
                        break;


                    case BillingClient.BillingResponseCode.ERROR:
                        Log.e(TAG, "onBillingSetupFinished -> Billing Response ERROR");
                        break;


                    case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                        Log.e(TAG, "onBillingSetupFinished -> Billing Response FEATURE_NOT_SUPPORTED");
                        break;

                    case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                        Log.e(TAG, "onBillingSetupFinished -> Billing Response ITEM_ALREADY_OWNED");
                        break;

                    case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
                        Log.e(TAG, "onBillingSetupFinished -> Billing Response ITEM_NOT_OWNED");
                        break;

                    case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                        Log.e(TAG, "onBillingSetupFinished -> Billing Response ITEM_UNAVAILABLE");
                        break;

                    case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                        Log.e(TAG, "onBillingSetupFinished -> Billing Response SERVICE_DISCONNECTED");
                        break;

                    case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                        Log.e(TAG, "onBillingSetupFinished -> Billing Response SERVICE_TIMEOUT");
                        break;

                    case BillingClient.BillingResponseCode.USER_CANCELED:
                        Log.e(TAG, "onBillingSetupFinished -> Billing Response USER_CANCELED");
                        break;

                    case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
                        Log.e(TAG, "onBillingSetupFinished -> Billing Response SERVICE_UNAVAILABLE");
                        break;
                }
            }
        };
    }

    private SkuDetailsResponseListener getSkuResponseListener() {
        return new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@NonNull @NotNull BillingResult billingResult, @Nullable @org.jetbrains.annotations.Nullable List<SkuDetails> list) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    if(list != null && list.size() != 0) {
                        List<SkuItem> items = new ArrayList<>();

                        for(SkuDetails skuDetails : list) {
                            items.add(new SkuItem(skuDetails, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startPurchaseFlow(skuDetails);
                                }
                            }));
                        }

                        mAdapter.setList(items);
                        mProductsRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mProductsRecyclerView.getAdapter().notifyDataSetChanged();
                            }
                        });

                        mBillingService.queryPurchases();

                        Log.e(TAG, "onSkuDetailsResponse -> List Updated Success");
                    }
                    else {
                        Log.e(TAG, "onSkuDetailsResponse -> List Is NULL or EMPTY");
                    }
                }
                else {
                    Log.e(TAG, "onSkuDetailsResponse -> Response Code Is Not OK");
                }

                switch (billingResult.getResponseCode()) {
                    case BillingClient.BillingResponseCode.OK:
                        Log.e(TAG, "onSkuDetailsResponse -> Billing Response OK");
                        break;

                    case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                        Log.e(TAG, "onSkuDetailsResponse -> Billing Response UNAVAILABLE");
                        break;


                    case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                        Log.e(TAG, "onSkuDetailsResponse -> Billing Response DEVELOPER_ERROR");
                        break;


                    case BillingClient.BillingResponseCode.ERROR:
                        Log.e(TAG, "onSkuDetailsResponse -> Billing Response ERROR");
                        break;


                    case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                        Log.e(TAG, "onSkuDetailsResponse -> Billing Response FEATURE_NOT_SUPPORTED");
                        break;

                    case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                        Log.e(TAG, "onSkuDetailsResponse -> Billing Response ITEM_ALREADY_OWNED");
                        break;

                    case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
                        Log.e(TAG, "onSkuDetailsResponse -> Billing Response ITEM_NOT_OWNED");
                        break;

                    case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                        Log.e(TAG, "onSkuDetailsResponse -> Billing Response ITEM_UNAVAILABLE");
                        break;

                    case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                        Log.e(TAG, "onSkuDetailsResponse -> Billing Response SERVICE_DISCONNECTEd");
                        break;

                    case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                        Log.e(TAG, "onSkuDetailsResponse -> Billing Response SERVICE_TIMEOUT");
                        break;

                    case BillingClient.BillingResponseCode.USER_CANCELED:
                        Log.e(TAG, "onSkuDetailsResponse -> Billing Response USER_CANCELED");
                        break;

                    case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
                        Log.e(TAG, "onSkuDetailsResponse -> Billing Response SERVICE_UNAVAILABLE");
                        break;
                }


            }
        };
    }

    private AcknowledgePurchaseResponseListener getAcknowledgeResponseListener() {
        return new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(@NonNull @NotNull BillingResult billingResult) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.e(TAG, "AcknowledgePurchaseResponseListener -> AcknowledgeResponse OK");
                }
            }
        };
    }

    private PurchasesResponseListener getPurchaseResponseListener() {
        return new PurchasesResponseListener() {
            @Override
            public void onQueryPurchasesResponse(@NonNull @NotNull BillingResult billingResult, @NonNull @NotNull List<Purchase> list) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    if(list != null && list.size() != 0) {
                        Log.e("Billing Service", "Query Purchases \n");
                        for(Purchase purchase: list) {
                            //Enable All Features
                            Log.e("Billing Service", "Is Purchased ? " + purchase.getPurchaseState());
                            if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                handlePurchase(purchase);
                                mBillingService.handlePurchase(purchase);
                            }
                        }
                    }
                }
            }
        };
    }

    @Override
    public void updateTheme() {
        super.updateTheme();
    }

    public View findViewById(int id) {
        return mActivity.findViewById(id);
    }
}
