/* OMNeTBridge class
 *
 * Michael Rosen
 * mrrosen
 * 4-27-2014
 *
 */

#include "OMNeTPipe.h"
#include "OMNeTPk.h"

#ifndef _OMNETBRIDGE_H_
#define _OMNETBRIDGE_H_

/* Variables */
#define BRIDGETYPE_CONTROL 2
#define BRIDGETYPE_SYSTEM  1
#define PORT_CONTROL       18100
#define PORT_SYSTEM        18240       

#define NUM_VARS          14

#define TEVAR_U_1          0
#define TEVAR_U_2          1
#define TEVAR_U_3          2
#define TEVAR_U_4          3
#define TEVAR_F_1          4
#define TEVAR_F_2          5
#define TEVAR_F_3          6
#define TEVAR_F_4          7
#define TEVAR_P            8
#define TEVAR_V_L          9
#define TEVAR_Y_A3        10
#define TEVAR_Y_B3        11
#define TEVAR_Y_C3        12
#define TEVAR_C           13

class OMNeTBridge {

 private:
  OMNeTPipe* pipe;
  int t;
  float time;
  void* var[14];
  
 public:
  OMNeTBridge(int t);
  float getVal(int v, float simTime);
  void setVal(int v, float tv, float simTime);
};

extern int controlBridgeMade;
extern int systemBridgeMade;
extern OMNeTBridge* controlBridge;
extern OMNeTBridge* systemBridge;


#endif /* _OMNETBRIDGE_H_ */
