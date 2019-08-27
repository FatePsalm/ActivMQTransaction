package com.solace.transactioncommon.enums;
 /**
   * 作者 CG
   * 时间 2019/8/27 10:27
   * 注释 最终一致性事务配置相关
   */
public class TStatusConfigEnum {
     /**
       * 作者 CG
       * 时间 2019/8/27 10:27
       * 注释 定时重发任务配置
       */
     public enum Timer{
         /**
          * 作者 CG
          * 时间 2019/8/27 10:29
          * 注释 重试次数
          */
         RETRY(6),
         /**
          * 作者 CG
          * 时间 2019/8/27 10:29
          * 注释 任务超时时间
          */
         TIME(3),
         ;
         private int value;

         Timer(int value) {
             this.value = value;
         }

         public int getValue() {
             return value;
         }

         public void setValue(int value) {
             this.value = value;
         }
     }
}
