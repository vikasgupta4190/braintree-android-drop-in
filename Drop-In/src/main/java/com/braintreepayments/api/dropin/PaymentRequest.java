package com.braintreepayments.api.dropin;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.braintreepayments.api.DataCollector;
import com.braintreepayments.api.PayPal;
import com.google.android.gms.identity.intents.model.CountrySpecification;
import com.google.android.gms.wallet.Cart;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to start {@link BraintreePaymentActivity} with specified options.
 */
public class PaymentRequest implements Parcelable {

    public static final String EXTRA_CHECKOUT_REQUEST = "com.braintreepayments.api.EXTRA_CHECKOUT_REQUEST";

    private String mAuthorization;

    private String mAmount;
    private boolean mCollectDeviceData;

    private Cart mAndroidPayCart;
    private boolean mAndroidPayShippingAddressRequired;
    private boolean mAndroidPayPhoneNumberRequired;
    private boolean mAndroidPayEnabled = true;
    private ArrayList<CountrySpecification> mAndroidAllowedCountriesForShipping = new ArrayList<>();

    private List<String> mPayPalAdditionalScopes;
    private boolean mPayPalEnabled = true;

    private boolean mVenmoEnabled = true;

    public PaymentRequest() {}

    /**
     * Provide authorization allowing this client to communicate with Braintree. Either
     * {@link #clientToken(String)} or {@link #tokenizationKey(String)} must be set or an
     * {@link com.braintreepayments.api.exceptions.AuthenticationException} will occur.
     *
     * @param clientToken The client token to use for the request.
     */
    public PaymentRequest clientToken(String clientToken) {
        mAuthorization = clientToken;
        return this;
    }

    /**
     * Provide authorization allowing this client to communicate with Braintree. Either
     * {@link #clientToken(String)} or {@link #tokenizationKey(String)} must be set or an
     * {@link com.braintreepayments.api.exceptions.AuthenticationException} will occur.
     *
     * @param tokenizationKey The tokenization key to use for the request.
     */
    public PaymentRequest tokenizationKey(String tokenizationKey) {
        mAuthorization = tokenizationKey;
        return this;
    }

    /**
     * This method is optional.
     *
     * @param amount Amount of the transaction.
     */
    public PaymentRequest amount(String amount) {
        mAmount = amount;
        return this;
    }

    /**
     * This method is optional.
     *
     * @param collectDeviceData {@code true} if Drop-in should collect and return device data for
     *        fraud prevention.
     * @see DataCollector
     */
    public PaymentRequest collectDeviceData(boolean collectDeviceData) {
        mCollectDeviceData = collectDeviceData;
        return this;
    }

    /**
     * This method is optional.
     *
     * @param cart The Android Pay {@link Cart} for the transaction.
     */
    public PaymentRequest androidPayCart(Cart cart) {
        mAndroidPayCart = cart;
        return this;
    }

    /**
     * This method is optional.
     *
     * @param shippingAddressRequired {@code true} if Android Pay requests should request a
     *        shipping address from the user.
     */
    public PaymentRequest androidPayShippingAddressRequired(boolean shippingAddressRequired) {
        mAndroidPayShippingAddressRequired = shippingAddressRequired;
        return this;
    }

    /**
     * This method is optional.
     *
     * @param phoneNumberRequired {@code true} if Android Pay requests should request a phone
     *        number from the user.
     */
    public PaymentRequest androidPayPhoneNumberRequired(boolean phoneNumberRequired) {
        mAndroidPayPhoneNumberRequired = phoneNumberRequired;
        return this;
    }

    /**
     * This method is optional.
     *
     * @param countryCodes countries to which shipping is supported.
     * Follows the ISO 3166-2 format (ex: "US", "CA", "JP")
     *
     * @see <a href="https://en.wikipedia.org/wiki/ISO_3166-2#Current_codes">ISO 3166 country codes</a>
     */
    public PaymentRequest androidPayAllowedCountriesForShipping(String... countryCodes) {
        mAndroidAllowedCountriesForShipping.clear();
        for(String countryCode : countryCodes) {
            mAndroidAllowedCountriesForShipping.add(new CountrySpecification(countryCode));
        }
        return this;
    }

    /**
     * Disables Android Pay in Drop-in.
     */
    public PaymentRequest disableAndroidPay() {
        mAndroidPayEnabled = false;
        return this;
    }

    /**
     * Set additional scopes to request when a user is authorizing PayPal.
     *
     * This method is optional.
     *
     * @param additionalScopes A {@link java.util.List} of additional scopes.
     *        Ex: PayPal.SCOPE_ADDRESS.
     *        Acceptable scopes are defined in {@link PayPal}.
     */
    public PaymentRequest paypalAdditionalScopes(List<String> additionalScopes) {
        mPayPalAdditionalScopes = additionalScopes;
        return this;
    }

    /**
     * Disables PayPal in Drop-in.
     */
    public PaymentRequest disablePayPal() {
        mPayPalEnabled = false;
        return this;
    }

    /**
     * Disables Venmo in Drop-in.
     */
    public PaymentRequest disableVenmo() {
        mVenmoEnabled = false;
        return this;
    }

    /**
     * Get an {@link Intent} that can be used in {@link android.app.Activity#startActivityForResult(Intent, int)}
     * to launch {@link BraintreePaymentActivity} and the Drop-in UI.
     *
     * @param context
     * @return {@link Intent} containing all of the options set in {@link PaymentRequest}.
     */
    public Intent getIntent(Context context) {
        return new Intent(context, BraintreePaymentActivity.class)
                .putExtra(EXTRA_CHECKOUT_REQUEST, this);
    }

    String getAuthorization() {
        return mAuthorization;
    }

    String getAmount() {
        return mAmount;
    }

    boolean shouldCollectDeviceData() {
        return mCollectDeviceData;
    }

    Cart getAndroidPayCart() throws NoClassDefFoundError {
        return mAndroidPayCart;
    }

    boolean isAndroidPayShippingAddressRequired() {
        return mAndroidPayShippingAddressRequired;
    }

    boolean isAndroidPayPhoneNumberRequired() {
        return mAndroidPayPhoneNumberRequired;
    }

    boolean isAndroidPayEnabled() {
        return mAndroidPayEnabled;
    }

    ArrayList<CountrySpecification> getAndroidPayAllowedCountriesForShipping() {
        return mAndroidAllowedCountriesForShipping;
    }

    List<String> getPayPalAdditionalScopes() {
        return mPayPalAdditionalScopes;
    }

    boolean isPayPalEnabled() {
        return mPayPalEnabled;
    }

    boolean isVenmoEnabled() {
        return mVenmoEnabled;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthorization);
        dest.writeString(mAmount);
        dest.writeByte(mCollectDeviceData ? (byte) 1 : (byte) 0);

        try {
            Cart.class.getClassLoader();
            dest.writeParcelable(mAndroidPayCart, 0);
            dest.writeByte(mAndroidPayShippingAddressRequired ? (byte) 1 : (byte) 0);
            dest.writeByte(mAndroidPayPhoneNumberRequired ? (byte) 1 : (byte) 0);
            dest.writeTypedList(mAndroidAllowedCountriesForShipping);
        } catch (NoClassDefFoundError ignored) {}

        dest.writeByte(mAndroidPayEnabled ? (byte) 1 : (byte) 0);
        dest.writeStringList(mPayPalAdditionalScopes);
        dest.writeByte(mPayPalEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(mVenmoEnabled ? (byte) 1 : (byte) 0);
    }

    protected PaymentRequest(Parcel in) {
        mAuthorization = in.readString();
        mAmount = in.readString();
        mCollectDeviceData = in.readByte() != 0;

        try {
            mAndroidPayCart = in.readParcelable(Cart.class.getClassLoader());
            mAndroidPayShippingAddressRequired = in.readByte() != 0;
            mAndroidPayPhoneNumberRequired = in.readByte() != 0;
            in.readTypedList(mAndroidAllowedCountriesForShipping, CountrySpecification.CREATOR);
        } catch (NoClassDefFoundError ignored) {}

        mAndroidPayEnabled = in.readByte() != 0;
        mPayPalAdditionalScopes = in.createStringArrayList();
        mPayPalEnabled = in.readByte() != 0;
        mVenmoEnabled = in.readByte() != 0;
    }

    public static final Creator<PaymentRequest> CREATOR = new Creator<PaymentRequest>() {
        public PaymentRequest createFromParcel(Parcel source) {
            return new PaymentRequest(source);
        }

        public PaymentRequest[] newArray(int size) {
            return new PaymentRequest[size];
        }
    };
}
