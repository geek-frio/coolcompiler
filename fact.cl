class Main inherits A2I{
    main() : Object {
        {
        (new IO).out_string(fact_loop(1));
        "a";
        }
    };

    fact(i: Int): Int {
        if (i = 0) then 1 else i * fact(i-1) fi
    };

    fact_loop(i: Int): String {
      {
          3+3;
          4+4;
          4<5;
          let a:Int in 5;
          let b: String in "55555\n";
      }
    };
};