/*package edu.columbia.dmi.TimeMotionTimer;*/

import java.util.Vector;

public class AProcess extends Vector {
	private String name;
	
	public AProcess(String name) {
		this.name = name;
	}
	
	public void stop(long time) { 
		for (int i=0;i<this.size();++i) {
			((EventCode) this.elementAt(i)).stop(time);
		}
	}
	
	public void stopOthers(EventCode current, long time) {
		for (int i=0;i<this.size();++i) {
			EventCode ev = (EventCode) this.elementAt(i);
			if (ev != current) {
				ev.stop(time);
			}
		}
	}

	public String getName() { return name; }
}
