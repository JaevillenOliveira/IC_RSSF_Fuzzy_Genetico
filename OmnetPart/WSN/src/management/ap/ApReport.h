/*
 * ApReport.h
 *
 *  Created on: Sep 10, 2019
 *      Author: jaevillen
 */

#ifndef MANAGEMENT_APREPORT_H_
#define MANAGEMENT_APREPORT_H_

using namespace omnetpp;

class ApReport: public cObject {
        float numberOfSta;
        float numberOfNeighbours;
        float rssiMean;
        float throughput;

    public:
        void setNumberOfSta(const float& a) { numberOfSta = a;}
        void setNumberOfNeighbours(const float& a) {numberOfNeighbours = a;}
        void setRssiMean(const float& a) {rssiMean = a;}
        const float& getNumberOfSta() const {return numberOfSta;}
        const float& getNumberOfNeighbours() const {return numberOfNeighbours;}
        const float& getRssiMean() const {return rssiMean; }
        const float& getThroughput() const {return throughput;}
        void setThroughput(const float& throughput) {this->throughput = throughput;}
};



#endif /* MANAGEMENT_APREPORT_H_ */
