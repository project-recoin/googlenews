package test;

public class TestImageUrl {

	public static void main(String[] args) {
		String a = "	\"imageUrl\" : \"background-image: url(\\\"https://s.yimg.com/bt/api/res/1.2/EEEOVf9D.9SHz9hoKf9FKA--/YXBwaWQ9eW5ld3NfbGVnbztmaT1maWxsO2g9NzU7cHlvZmY9MjU7cT03NTt3PTEwMA--/http://media.zenfs.com/en_us/News/Reuters/2015-11-18T210749Z_1_LYNXNPEBAH1BP_RTROPTP_2_NIGERIA-VIOLENCE-KANO.JPG\\\");\"\n"
				+ "";

		String cleanUrl = a.substring(a.lastIndexOf("--/"),
				a.lastIndexOf("\\\");"));

		System.out.println(cleanUrl);
	}

}
