//IRemoteService.aidl
package edu.tjrac.swant.wjzx.aidl.bean;

import edu.tjrac.swant.wjzx.aidl.bean.IRemoteServiceCallback;

interface IRemoteService{

   int pid();

   void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
             double aDouble, String aString);

   void hello();

   void registerCallback(IRemoteServiceCallback callback);

}