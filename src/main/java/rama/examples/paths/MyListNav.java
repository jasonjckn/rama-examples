package rama.examples.paths;

import com.rpl.rama.*;
import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MyListNav implements Navigator<List> {
  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Object select(List obj, Next next) {
    if(obj.size() < 2) return Navigator.VOID;
    else {
      Object ret = next.invokeNext(obj.get(0));
      next.invokeNext(obj.get(1));
      return ret;
    }

  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public List transform(List obj, Next next) {
    if(obj.size() >= 2) {
      Object new0 = next.invokeNext(obj.get(0));
      Object new1 = next.invokeNext(obj.get(1));
      if(new1 == Navigator.VOID) obj.remove(1);
      else obj.set(1, new1);
      if(new0 == Navigator.VOID) obj.remove(0);
      else obj.set(0, new0);
    }

    return obj;
  }
}
