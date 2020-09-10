function Server
    fis = readfis('ComnetUniformSetsV2.fis');   %readfis('TempSolution.fis');

    pipe = OMNeTPipe("localhost", 18638);
    responseNames = 'respPk';
    for k=1:Inf
        [headerName, pktMap] = recvPk(pipe);

        if(headerName == "endingPacket")
            exit
        end

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

    end
end








