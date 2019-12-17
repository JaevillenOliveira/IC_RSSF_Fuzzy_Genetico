/*
 * Throughput.h
 *
 *  Created on: Sep 9, 2019
 *      Author: jaevillen
 */

#ifndef APPLICATION_THROUGHPUT_H_
#define APPLICATION_THROUGHPUT_H_

using namespace omnetpp;

namespace WSN{


class ThroughputReport : public cObject{
    private:
        double throughput;
    public:
        double getThroughput() const {return throughput;}
        void setThroughput(double throughput) {this->throughput = throughput;}
};


}
#endif /* APPLICATION_THROUGHPUT_H_ */
