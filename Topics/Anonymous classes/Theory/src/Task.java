// You can experiment here, it won’t be checked

interface SingleMethodInterface {

  void doSomething();
}

public class Task {
  public static void main(String[] args) {
    SingleMethodInterface instance = new SingleMethodInterface() {

      @Override
      public void doSomething() {
        System.out.println("The anonymous class does something");
      }
    };

    instance.doSomething();
  }
}
