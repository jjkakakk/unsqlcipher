package com.jjk.unsqlcipher;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.widget.Toast;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findConstructorExact;
import static de.robv.android.xposed.XposedHelpers.setBooleanField;
import static de.robv.android.xposed.XposedHelpers.setIntField;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.callbacks.XCallback;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XC_MethodHook;
import static de.robv.android.xposed.XposedHelpers.findClass;


//public class UnSQLCipher implements IXposedHookLoadPackage {

public class UnSQLCipher implements IXposedHookLoadPackage {

	
	public static final String TAG = "unsqlcipher";
	
	private static void melog(String string, String apkname ) {
		Log.v( TAG, TAG + ":" + apkname + " : " + string); 
	} 

	// REPLACEMENT METHODS
//	XC_MethodReplacement returnTrue = new XC_MethodReplacement() {
//		@Override
//		protected Object replaceHookedMethod(MethodHookParam param)	throws Throwable {
//			melog( "in hook replace - return true / " + param.method.toString(), param.toString() );
//			return Boolean.valueOf(true);
//		}
//	};
//	XC_MethodReplacement returnFalse = new XC_MethodReplacement() {
//		@Override
//		protected Object replaceHookedMethod(MethodHookParam param)	throws Throwable {
//			melog( "in hook replace - return false / " + param.method.toString(),  param.toString() );
//			return Boolean.valueOf(false);
//		}
//	};
//	XC_MethodReplacement returnZero = new XC_MethodReplacement() {
//		@Override
//		protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
//			melog( "in hook replace - return 0 / " + param.method.toString(), param.toString() );
//			return 0;
//		}
//	};
	
	
	private void hookCipher( final LoadPackageParam lpparam, String apkname ) {
		final String aname = apkname;
		try {
			final Class<?> CLASS_CursorFactory = findClass( "net.sqlcipher.database.SQLiteDatabase.CursorFactory", lpparam.classLoader);
 			final Class<?> CLASS_SQLiteDatabaseHook = findClass( "net.sqlcipher.database.SQLiteDatabaseHook" , lpparam.classLoader );
			final Class<?> CLASS_SQLiteDatabase = findClass( "net.sqlcipher.database.SQLiteDatabase" , lpparam.classLoader );
			//Constructor<?> CONS_SQLiteDatabase = findConstructorExact(CLASS_SQLiteDatabase, String.class, String.class, CursorFactory.class, int.class );
			//Constructor<?> CONS_SQLiteDatabase_a = findConstructorExact(CLASS_SQLiteDatabase, String.class, String.class, CursorFactory.class, int.class, CLASS_SQLiteDatabaseHook );
		
			Constructor<?> CONS_SQLiteDatabase_a = XposedHelpers.findConstructorBestMatch(CLASS_SQLiteDatabase, String.class, String.class, CLASS_CursorFactory, int.class, CLASS_SQLiteDatabaseHook );
			XposedBridge.hookMethod(CONS_SQLiteDatabase_a, new XC_MethodHook(XCallback.PRIORITY_HIGHEST) {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					String s = (String)param.args[0];
					String s1 = (String)param.args[1];
					melog( "unsqlcipher before Constructor net.sqlcipher.database.SQLiteDatabase, got s:" + s + ", s1:" + s1, aname );
				}
		//			@Override
		//			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
		//				melog ("ahook Constructor" , apkname );
		//			}
			});
		} catch (Throwable E) {
			melog( "could not find and hook  : constructor net.sqlcipher.database.SQLiteDatabase_a : " + E.toString() +"\r\n" + E.getStackTrace().toString(), apkname );
		}
		
	
		// 130     public static SQLiteDatabase create(CursorFactory cursorfactory, String s)
		try { 
			final Class<?> CLASS_CursorFactory = findClass( "net.sqlcipher.database.SQLiteDatabase.CursorFactory", lpparam.classLoader);
			findAndHookMethod("net.sqlcipher.database.SQLiteDatabase", lpparam.classLoader, "create", CLASS_CursorFactory, String.class, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod( MethodHookParam param ) throws Throwable {
					String s1 = (String) param.args[1];
					melog( "unsqlcipher - hook net.sqlcipher.database.SQLiteDatabase, captured s1:" + s1 , aname );
				}
			});		
		} catch ( Throwable e ) {
			melog( "unsqlcipher net.sqlcipher.database.SQLiteDatabase error create - " + e.toString() + "\r\n" + e.getStackTrace().toString() , lpparam.packageName );
		}
		// 392     public static SQLiteDatabase openDatabase(String s, String s1, CursorFactory cursorfactory, int i)
		try {
			final Class<?> CLASS_CursorFactory = findClass( "net.sqlcipher.database.SQLiteDatabase.CursorFactory", lpparam.classLoader);
			findAndHookMethod("net.sqlcipher.database.SQLiteDatabase", lpparam.classLoader, "openDatabase", String.class, String.class, CLASS_CursorFactory, int.class, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod( MethodHookParam param ) throws Throwable {
					String s = (String)param.args[0];
					String s1 = (String) param.args[1];
					melog( "unsqlcipher - hook net.sqlcipher.database.SQLiteDatabase, captured s:" + s + " s1:" + s1 , aname );
				}
			});		
		} catch ( Throwable e ) {
			melog( "unsqlcipher error opendatabase - " + e.toString() + "\r\n" + e.getStackTrace().toString() , lpparam.packageName );
		}
		// 397     public static SQLiteDatabase openDatabase(String s, String s1, CursorFactory cursorfactory, int i, SQLiteDatabaseHook sqlitedatabaseh
		try { 
			final Class<?> CLASS_CursorFactory = findClass( "assh er.database.SQLiteDatabase.CursorFactory", lpparam.classLoader);
			final Class<?> CLASS_SQLiteDatabaseHook = findClass( "net.sqlcipher.database.SQLiteDatabaseHook" , lpparam.classLoader );
			findAndHookMethod("net.sqlcipher.database.SQLiteDatabase", lpparam.classLoader, "openDatabase", String.class, String.class, CLASS_CursorFactory, 
					int.class, CLASS_SQLiteDatabaseHook,  new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod( MethodHookParam param ) throws Throwable {
					String s = (String)param.args[0];
					String s1 = (String) param.args[1];
					melog( "unsqlcipher - hook net.sqlcipher.database.SQLiteDatabase, captured s:" + s + " s1:" + s1 , aname );
				}
			});		
		} catch ( Throwable e ) {
			melog( "unsqlcipher error opendatabase h - " + e.toString() + "\r\n" + e.getStackTrace().toString() , lpparam.packageName );
		}
	}

	
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		try {
    		String apkname = lpparam.packageName;
    		if ( apkname.toLowerCase().contains("com.rim.mobilefusion".toLowerCase() ) ) {
	    		try {
	    			melog( "loading com.rim.mobilefusion", lpparam.packageName );
	    			hookCipher( lpparam, apkname );
	        	} catch ( Throwable e ) {
	        		melog( "error: SHOULDNT BE THROWN HERE!! " + e.toString() + "\r\n" + e.getStackTrace().toString(), lpparam.packageName );
	        	}
    		}
    	} catch ( Exception e ) {
    		melog( "error: SHOULDNT BE THROWN HERE!! " + e.toString() + "\r\n" + e.getStackTrace().toString(), lpparam.packageName );
    	}
	}

//	@Override
//	public void initZygote(StartupParam startupParam) throws Throwable {
//		try {
//    		String apkname = lpparam.packageName;
//    		if ( apkname.toLowerCase().contains("net.sqlcipher.database".toLowerCase() ) ) {
//	    		try {
//	    			melog( "loading com.rim.mobilefusion", lpparam.packageName );
//	    			initBes( lpparam, apkname );
//	        	} catch ( Throwable e ) {
//	        		melog( "error: SHOULDNT BE THROWN HERE!! " + e.toString() + "\r\n" + e.getStackTrace().toString(), lpparam.packageName );
//	        	}
//    		}
//    	} catch ( Exception e ) {
//    		melog.
//    	}
//	}

}
