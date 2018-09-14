import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

// David Mendes,    #44934
// Francisco Cunha, #45412

/*@
  predicate_ctor SensorInt_shared_state(SensorInt s)() =
    s.value   |-> ?v
    &*& s.min |-> ?low
    &*& s.max |-> ?high
    &*& low <= high
    &*& v >= low
    &*& v <= high
    ;
@*/

/*@
  predicate SensorIntInv(SensorInt s;) =
    s.mon |-> ?m
    &*& m != null
    &*& lck(m, 1, SensorInt_shared_state(s))
    ;
@*/

class SensorInt {

  int value;
  final int min;
  final int max;
  final Thread th;

  final ReentrantLock mon;
  
  SensorInt(int min, int max)
    //@ requires min <= max &*& [_]System_out(?o) &*& o != null &*& [_]TimeUnit_SECONDS(?s) &*& s != null;
    //@ ensures [_]SensorIntInv(this);
  {
    this.min = min;
    this.max = max;
    this.value = min;
    //@ close SensorInt_shared_state(this)();

    //@ close enter_lck(1, SensorInt_shared_state(this));
    this.mon = new ReentrantLock();
    //@ close [1/2]SensorIntInv(this);
  
    this.th = new Thread(new Probe(this));
    this.th.start();
  }
  
  int get()
    //@ requires [?f]SensorIntInv(this);
    //@ ensures [f]SensorIntInv(this);
  {
    //@open SensorIntInv(this);
    int result;
    mon.lock();
    //@open SensorInt_shared_state(this)();
    result = this.value;
    //@ close SensorInt_shared_state(this)();
    mon.unlock();
    //@ close [f]SensorIntInv(this);
    return result;
  }
  
  int getMin()
    //@ requires [?f]SensorIntInv(this);
    //@ ensures [f]SensorIntInv(this);
  {
    //@open SensorIntInv(this);
    int result;
    mon.lock();
    //@open SensorInt_shared_state(this)();
    result = this.min;
    //@ close SensorInt_shared_state(this)();
    mon.unlock();
    //@ close [f]SensorIntInv(this);
    return result;
  }
  
  int getMax()
    //@ requires [?f]SensorIntInv(this);
    //@ ensures [f]SensorIntInv(this);
  {
    //@open SensorIntInv(this);
    int result;
    mon.lock();
    //@open SensorInt_shared_state(this)();
    result = this.max;
    //@ close SensorInt_shared_state(this)();
    mon.unlock();
    //@ close [f]SensorIntInv(this);
    return result;
  }
  
  void set(int value)
    //@ requires [?f]SensorIntInv(this);
    //@ ensures [f]SensorIntInv(this);
  {
    //@ open SensorIntInv(this);
    mon.lock();
    //@ open SensorInt_shared_state(this)();
    if (value >= min && value <= max) {
      this.value = value;
    }
    //@ close SensorInt_shared_state(this)();
    mon.unlock();
    //@ close [f]SensorIntInv(this);  
    }

  public static void main(String args[])
    throws InterruptedException /*@ ensures true; @*/
    //@ requires true &*& [_]System_out(?o) &*& o != null &*& [_]TimeUnit_SECONDS(?s) &*& s != null;
    //@ ensures true;
  {
    SensorInt sensor = new SensorInt(0, 10);
    
    while (true)
      //@ invariant [_]SensorIntInv(sensor) &*& [_]System_out(?z) &*& z != null &*& [_]TimeUnit_SECONDS(?t) &*& t != null;
    {
      int r = sensor.get();
      TimeUnit.SECONDS.sleep(5);
      System.out.println("Generated " + Integer.toString(r));
    }    
  }
}

// ---------------------------------------------------------------------------------------------------------------------------------------

/*@ 
  predicate ProbeInv(Probe p;) =
    p.sensor |-> ?s
    &*& s != null
    &*& [_]SensorIntInv(s)
    ;
@*/

class Probe implements Runnable {

  //@ predicate pre() = ProbeInv(this) &*& [_]TimeUnit_SECONDS(?s) &*& s != null &*& [_]System_out(?o) &*& o != null;
  //@ predicate post() = true;

  private SensorInt sensor;

  public Probe(SensorInt s)
    //@ requires s != null &*& [?f]SensorIntInv(s) &*& [_]TimeUnit_SECONDS(?t) &*& t != null &*& [_]System_out(?o) &*& o != null;
    //@ ensures pre();
  {
    this.sensor = s;
    //@ close pre();
  }
  
  public void run()
    //@ requires pre();
    //@ ensures post();
  {
  
    int sensorMin = sensor.getMin();
    int sensorMax = sensor.getMax();
    Random r = new Random();
    
    while (true)
      /*@ invariant
      	ProbeInv(this) &*&
      	[_]System_out(?o) &*& o != null &*& 
        [_]TimeUnit_SECONDS(?s) &*& s != null;
      @*/
   {
      int n = sensorMin + r.nextInt(sensorMax - sensorMin + 1);
      TimeUnit.SECONDS.sleep(2);
      System.out.println("Generated " + Integer.toString(n));
      sensor.set(n);
   }
  }
}
