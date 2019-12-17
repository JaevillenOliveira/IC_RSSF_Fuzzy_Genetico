/* OMNeTBridge class
 *
 * Michael Rosen
 * mrrosen
 * 4-27-2014
 *
 */

#include "OMNeTBridge.h"
#include <stdio.h>
#include <iostream>
using std::cout;
using std::cerr;
using std::endl;

int controlBridgeMade = 0;
int systemBridgeMade = 0;
OMNeTBridge* controlBridge;
OMNeTBridge* systemBridge;

/* Constructor */
OMNeTBridge::OMNeTBridge(int cType) {
  /* Local vars */
  OMNeTPk pk ("UPDATE");
  OMNeTPk* rpk;
  float dt = 0.0001;

  /* Set the type of this bridge */
  t = cType;

  /* Make a new pipe */
  if (cType == BRIDGETYPE_CONTROL) {
	pipe = new OMNeTPipe("localhost", PORT_CONTROL);
  }
  else {
	pipe = new OMNeTPipe("localhost", PORT_SYSTEM);
  }

  /* Initialize the connection and MATLAB model */
  pk.addVal("id", (void*)1, TYPE_INT);
  pk.addVal("dt", FLOAT(dt), TYPE_FLOAT);
  pipe->sendPk(pk);

  /* Receive the status packet with everything inside */
  rpk = pipe->recvPk();

  var[TEVAR_U_1] = rpk->getVal(12);
  var[TEVAR_U_2] = rpk->getVal(13);
  var[TEVAR_U_3] = rpk->getVal(14);
  var[TEVAR_U_4] = rpk->getVal(15);
  var[TEVAR_F_1] = rpk->getVal(2);
  var[TEVAR_F_2] = rpk->getVal(3);
  var[TEVAR_F_3] = rpk->getVal(4);
  var[TEVAR_F_4] = rpk->getVal(5);
  var[TEVAR_P] = rpk->getVal(6);
  var[TEVAR_V_L] = rpk->getVal(7);
  var[TEVAR_Y_A3] = rpk->getVal(8);
  var[TEVAR_Y_B3] = rpk->getVal(9);
  var[TEVAR_Y_C3] = rpk->getVal(10);
  var[TEVAR_C] = rpk->getVal(11);
  
  delete rpk;
}

/* Gats a variable from the current state after updating the state */
float OMNeTBridge::getVal(int v, float simTime) {
  /* Local vars */
  OMNeTPk pk ("UPDATE");
  OMNeTPk* rpk;
  float dt = simTime - time;

  /* Update time */
  time = simTime;

  cout << "GET: Getting Values from MATLAB...." << t << endl;

  /* Initialize the connection and MATLAB model */
  pk.addVal("id", (void*)1, TYPE_INT);
  pk.addVal("dt", FLOAT(dt), TYPE_FLOAT);
  pipe->sendPk(pk);

  /* Receive the status packet with everything inside */
  rpk = pipe->recvPk();

  cout << "GET: GOT SOME CHOCOLATE!" << endl;

  if (t == BRIDGETYPE_CONTROL) {
      var[TEVAR_U_1] = rpk->getVal(12);
      var[TEVAR_U_2] = rpk->getVal(13);
      var[TEVAR_U_3] = rpk->getVal(14);
      var[TEVAR_U_4] = rpk->getVal(15);
  } else {
      var[TEVAR_F_1] = rpk->getVal(2);
      var[TEVAR_F_2] = rpk->getVal(3);
      var[TEVAR_F_3] = rpk->getVal(4);
      var[TEVAR_F_4] = rpk->getVal(5);
      var[TEVAR_P]    = rpk->getVal(6);
      var[TEVAR_V_L]  = rpk->getVal(7);
      var[TEVAR_Y_A3] = rpk->getVal(8);
      var[TEVAR_Y_B3] = rpk->getVal(9);
      var[TEVAR_Y_C3] = rpk->getVal(10);
      var[TEVAR_C] = rpk->getVal(11);
  }
  cout << "GET: Complete packet parse!" << endl;

  delete rpk;
  
  cout << "GET: BYE" << endl;
  float * f = (float *)&(var[v]);
  cout << "GETTING " << v << " : " << *f << endl;
  return *f;
}

/* Sets a varue in the bridge and sends it to MATLAB */
void OMNeTBridge::setVal(int v, float tv, float simTime) {
  /* Local vars */
  OMNeTPk *pk = new OMNeTPk("CHANGE");
  OMNeTPk* rpk;
  float dt = simTime - time;

  /* Update time */
  time = simTime;

  cout << "SET: SETTING VAL var[" << v << "] tv" << tv << endl;

  /* Set the varue */
  void ** vv = (void **)&tv;
  var[v] = *vv;

  /* Initialize the connection and MATLAB model */
  pk->addVal("id", (void*)1, TYPE_INT);
  pk->addVal("dt", FLOAT(dt), TYPE_FLOAT);

  /* Add the needed variables */
  if (t == BRIDGETYPE_CONTROL) {
	pk->addVal("F_1", FLOAT(var[TEVAR_F_1]), TYPE_FLOAT);
	pk->addVal("F_2", FLOAT(var[TEVAR_F_2]), TYPE_FLOAT);
	pk->addVal("F_3", FLOAT(var[TEVAR_F_3]), TYPE_FLOAT);
	pk->addVal("F_4", FLOAT(var[TEVAR_F_4]), TYPE_FLOAT);
	pk->addVal("P", FLOAT(var[TEVAR_P]), TYPE_FLOAT);
	pk->addVal("V_L", FLOAT(var[TEVAR_V_L]), TYPE_FLOAT);
	pk->addVal("y_a3", FLOAT(var[TEVAR_Y_A3]), TYPE_FLOAT);
	pk->addVal("y_b3", FLOAT(var[TEVAR_Y_B3]), TYPE_FLOAT);
	pk->addVal("y_c3", FLOAT(var[TEVAR_Y_C3]), TYPE_FLOAT);
	pk->addVal("C", FLOAT(var[TEVAR_C]), TYPE_FLOAT);
  }
  else {
	pk->addVal("u_1", FLOAT(var[TEVAR_U_1]), TYPE_FLOAT);
	pk->addVal("u_2", FLOAT(var[TEVAR_U_2]), TYPE_FLOAT);
	pk->addVal("u_3", FLOAT(var[TEVAR_U_3]), TYPE_FLOAT);
	pk->addVal("u_4", FLOAT(var[TEVAR_U_4]), TYPE_FLOAT);
  }

  cout << "SET: PACKET FORMED, SENDING...." << t << endl;

  pipe->sendPk(*pk);

  /* Receive the status packet with everything inside */
  rpk = pipe->recvPk();

  cout << "SET: GOT CHOCOLATE, EATING..." << endl;

  if (t == BRIDGETYPE_CONTROL) {
      var[TEVAR_U_1] = rpk->getVal(12);
      var[TEVAR_U_2] = rpk->getVal(13);
      var[TEVAR_U_3] = rpk->getVal(14);
      var[TEVAR_U_4] = rpk->getVal(15);
  } else {
      var[TEVAR_F_1] = rpk->getVal(2);
      var[TEVAR_F_2] = rpk->getVal(3);
      var[TEVAR_F_3] = rpk->getVal(4);
      var[TEVAR_F_4] = rpk->getVal(5);
      var[TEVAR_P] = rpk->getVal(6);
      var[TEVAR_V_L] = rpk->getVal(7);
      var[TEVAR_Y_A3] = rpk->getVal(8);
      var[TEVAR_Y_B3] = rpk->getVal(9);
      var[TEVAR_Y_C3] = rpk->getVal(10);
      var[TEVAR_C] = rpk->getVal(11);
  }
  cout << "SET: DELETE!" << endl;

  delete rpk;
  cout << "SET: BYE" << endl;
  //delete pk;
  cout << "SET: BY2" << endl;

  return;
}
