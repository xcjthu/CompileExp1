package submit;

class TestFaintness {
    /**
     * In this method all variables are faint because the final value is never used.
     * Sample out is at src/test/Faintness.out
     */
    void test1() {
        int x = 2;
        int y = x + 2;
        int z = x + y;
        return;
    }

    /**
     * Write your test cases here. Create as many methods as you want.
     * Run the test from root dir using
     * ./run.sh flow.Flow submit.MySolver submit.Faintness submit.TestFaintness
     */

      int test2() {
          int x = 1;
          int y = x + 2;
          int z = y * 3;
          return x; // y, z is faint
      }

      int test3(){
          int x = 1;
          int y = x + 2;
          int z = y * 3;
          return z; // no variable is faint
      }

      void test4(){
        boolean x = true;
        int y = 100;
        if (x){
            y = -1;
        }
        return; // y is faint
      }

      void test5(){
          int x = 0, i = 0;
          for (; i < 100; ++ i){
              x += 1;
          }
          // x is faint
      }

      void test6(int step, int n){
          if (n < 0) return;
          test6(step, n - 1);
          // no variable is faint
      }

}
