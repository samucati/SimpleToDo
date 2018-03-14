package apps.jizzu.simpletodo.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import apps.jizzu.simpletodo.BuildConfig;
import apps.jizzu.simpletodo.R;
import apps.jizzu.simpletodo.utils.DeviceInfo;
import apps.jizzu.simpletodo.utils.PreferenceHelper;

/**
 * Fragment which contains the settings information.
 */
public class SettingsFragment extends PreferenceFragment {

    PreferenceHelper mPreferenceHelper;
    CheckBoxPreference mAnimation;
    CheckBoxPreference mGeneralNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        mPreferenceHelper = PreferenceHelper.getInstance();

        mAnimation = (CheckBoxPreference) findPreference("animation");
        mAnimation.setChecked(mPreferenceHelper.getBoolean(PreferenceHelper.ANIMATION_IS_ON));

        mAnimation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                mPreferenceHelper.putBoolean(PreferenceHelper.ANIMATION_IS_ON, mAnimation.isChecked());

                return true;
            }
        });

        mGeneralNotification = (CheckBoxPreference) findPreference("general_notification");
        mGeneralNotification.setChecked(mPreferenceHelper.getBoolean(PreferenceHelper.GENERAL_NOTIFICATION_IS_ON));

        mGeneralNotification.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                mPreferenceHelper.putBoolean(PreferenceHelper.GENERAL_NOTIFICATION_IS_ON, mGeneralNotification.isChecked());

                return true;
            }
        });

        Preference rateThisApp = findPreference("rate_app");
        rateThisApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final String appPackageName = getActivity().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return true;
            }
        });

        Preference sendFeedback = findPreference("send_feedback");
        sendFeedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent email = new Intent(Intent.ACTION_SENDTO);
                email.setData(new Uri.Builder().scheme("mailto").build());
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"ilya.ponomarenko.dev@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_title));
                email.putExtra(Intent.EXTRA_TEXT, getString(R.string.feedback_device_info) + "\n" + DeviceInfo.getDeviceInfo()
                        + "\n" + getString(R.string.feedback_app_version) + BuildConfig.VERSION_NAME
                        + "\n" + getString(R.string.feedback) + "\n");
                try {
                    startActivity(Intent.createChooser(email, "Send feedback"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText((getActivity()), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        Preference licences = findPreference("about_licenses");
        licences.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), LicensesActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }
}
