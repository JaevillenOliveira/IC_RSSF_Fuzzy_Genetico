	- Packet load adjusted: before the interval was according to a 9375Byte packet load, now 
		the time between packets sending is the right one according to the packet size
		equal to 1500Byte and the traffic pattern discussed in the article
		
	- This simulation still uses the poisson distribution to coordinate the packets
		sending interval
