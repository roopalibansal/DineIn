package com.cloud.dinein;


import android.content.Intent;
import android.util.Pair;
public interface AsyncResponse {
	void processFinish(Pair<String,Intent> locIntentPair);
}
