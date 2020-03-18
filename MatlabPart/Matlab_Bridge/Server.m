fis = readfis('ComnetUniformSetsV1');

pipe = OMNeTPipe("localhost", 18638);
responseNames = 'respPk';
while 1
    try
        [headerName, pktMap] = recvPk(pipe);

        disp(headerName);
        sensors = pktMap('sensors');
        neighbors = pktMap('neighbors');
        rssi = pktMap('rssi');
        throughput = pktMap('throughput');

        fprintf("Sensors:");
        disp(sensors)
        fprintf("Neighbors:");
        disp(neighbors)
        fprintf("RSSI:");
        disp(rssi)
        fprintf("Throughput:");
        disp(throughput)

        output = evalfis(fis,[rssi neighbors sensors throughput]);   
        respName{k} = [responseNames '_' num2str(k,'%d')];
        sendPk(pipe, 'fuzzyResp', output, pipe.typeFloat, respName{k});
        disp(respName{k});
        disp(output);
    catch
       % break;
    end

end








