package com.sopovs.moradanen.jcstress.guava;

import java.io.Serializable;

import org.openjdk.jcstress.annotations.Result;

@Result
@SuppressWarnings("serial")
public class ServiceResult implements Serializable {
    @sun.misc.Contended
    @jdk.internal.vm.annotation.Contended
	public boolean started, stopped;
    @sun.misc.Contended
    @jdk.internal.vm.annotation.Contended
	public boolean running, starting;
    @sun.misc.Contended
    @jdk.internal.vm.annotation.Contended
	public String stopping, terminated, failed;

	@Override
	public String toString() {
		return started + ", " + stopped + ", "
				+ running + ", " + starting + ", "
				+ stopping + ", " + terminated + ", " + failed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((failed == null) ? 0 : failed.hashCode());
		result = prime * result + (running ? 1231 : 1237);
		result = prime * result + (started ? 1231 : 1237);
		result = prime * result + (starting ? 1231 : 1237);
		result = prime * result + (stopped ? 1231 : 1237);
		result = prime * result + ((stopping == null) ? 0 : stopping.hashCode());
		result = prime * result + ((terminated == null) ? 0 : terminated.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceResult other = (ServiceResult) obj;
		if (failed == null) {
			if (other.failed != null)
				return false;
		} else if (!failed.equals(other.failed))
			return false;
		if (running != other.running)
			return false;
		if (started != other.started)
			return false;
		if (starting != other.starting)
			return false;
		if (stopped != other.stopped)
			return false;
		if (stopping == null) {
			if (other.stopping != null)
				return false;
		} else if (!stopping.equals(other.stopping))
			return false;
		if (terminated == null) {
			if (other.terminated != null)
				return false;
		} else if (!terminated.equals(other.terminated))
			return false;
		return true;
	}

}