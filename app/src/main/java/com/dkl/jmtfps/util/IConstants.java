package com.dkl.jmtfps.util;

/**
 * Constants definition
 */
public interface IConstants {

	int MSG_ENROLL =901;
	int MSG_VERIFY =902;
	int MSG_CANCEL =903;
	
	
    // EnrollFinger
    int MSG_THREAD_ENROLL_FIG 		= 1;
    int MSG_THREAD_ENROLL_OK 		= 2;

    // FingerprintLockService
    int MSG_START_UI				= 3;
    int MSG_REMOVE_UI				= 4;
    int MSG_STOP_UI				    = 5;
    int MSG_AUTO_SUCCESSFUL		    = 6;
    int MSG_SUCCESSFUL				= 7;
    int MSG_FAILURE 				= 8;
    int MSG_ABORT_FINGER 			= 16;

    // Advice
    int MSG_NOTIFY_LEAVE_FINGER	    = 9;
    int MSG_TOO_WET				    = 10;
    int MSG_TOO_SMALL				= 11;
    int MSG_ADJUST					= 12;
    int MSG_WAIT_FINGER			    = 13;
    int MSG_CHANGE_FINGER			= 14;
    int MSG_OTHER					= 15;
}
