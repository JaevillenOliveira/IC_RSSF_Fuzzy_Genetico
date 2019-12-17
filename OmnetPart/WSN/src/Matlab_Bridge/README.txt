C++:

First, you need to #include "OMNeTPk.hpp" and "OMNeTPipe.hpp".
Then, create a new OMNeTPipe object:

OMNeTPipe* p = new OMNeTPipe("localhost", 18637);

The matlab pipe object has to already be created, as it is the server for the
connection.  We are using the port 18637 for all communications at this point.

Then, create a packet:

OMNeTPk* pk = new OMNeTPk("headerName");

This takes in a header name that will be sent to differentiate packets.

Then, to add fields to this packet you do the following:

pk->addVal("fieldName", fieldValue, fieldType);

And do this as many times as needed.  The types used with matlab are
TYPE_FLOAT.

Sending:

To send a packet, just use p->sendPk(*pk);

Recieving:

To recieve a packet, just use p->recvPk();

To get the header of the packet

p->getHeader()

To get the size

p->getSize()

To get a specific field:

name = p->getName(indexOfVariable);
p->getVal(name);



MATLAB:

The OMNeTPipe works as follows for matlab:

Create an OMNeTPipe object.
This creates a tunnel where matlab is the server.
pipe = OMNeTPipe()

Receiving:

recvPk(pipe) will receive a packet and output the vector [headerName, pktMap]
The packet map is easy to use, as all you have to do is access it by:
pktMap('fieldName')

Sending:

sendPk(pipe, 'headerName', field1_value, field1_type, 'field1_name',
field2_value, ...)

This takes in the pipe as well as a header name, to differentiate (has to be
short) and fields in the order of (value, type, name).
