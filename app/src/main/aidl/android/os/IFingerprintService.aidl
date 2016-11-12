/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.os;
import android.os.IFingerprintCallBack;
interface IFingerprintService {
	
	int initSensor(IFingerprintCallBack CallBack);

	int GetSensorType();

	int CancelAction();

	int Verify();

	int SetEnrollCounts(int cnt);

	int Enrollment();
	
	int Erase(int index);
	
	int EraseFull();

	int SetWaitTouch();

	int CancelWaitTouch();

	int WaitClick();

	int StartNavigation();

	String GetSDKVersion();

	int GetOneFinger();

	int GetFingerLoop();
	
	int SystemWakeup();
	
	int SetInputKey(int key);
}
