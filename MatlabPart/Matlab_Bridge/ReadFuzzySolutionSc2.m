function ReadFuzzySolution
    config = fopen('/home/jaevillen/IC/Buffer/ConfigFileSc2.txt','r');
    fgetl(config); %% Reads the name
    fgetl(config); %% Reads configurations ... 
    upperLimits = fscanf(config,'%i', [1, 5]);
    lowerLimits = fscanf(config,'%i', [1, 5]);
    variableNames = ["rssi", "neighbors", "sources", "throughput", "switch_perc"];

    fis = mamfis;
    fis = addInput(fis,[lowerLimits(1) upperLimits(1)],"Name","rssi");
    fis = addInput(fis,[lowerLimits(2) upperLimits(2)],"Name","neighbors");
    fis = addInput(fis,[lowerLimits(3) upperLimits(3)],"Name","sources");
    fis = addInput(fis,[lowerLimits(4) upperLimits(4)],"Name","throughput");
    fis = addOutput(fis,[lowerLimits(5) upperLimits(5)],"Name","switch_perc");

    fid = fopen('/home/jaevillen/IC/Buffer/TempSolution.txt','r');
    
    for variable = variableNames %% The five variables, includind the one output    
        A = fscanf(fid,'%f', [1, 3]);
        B = fscanf(fid,'%f', [1, 3]);
        C = fscanf(fid,'%f', [1, 3]);
        fis = addMF(fis,variable,"trimf",A);
        fis = addMF(fis,variable,"trimf",B);
        fis = addMF(fis,variable,"trimf",C);
    end
    for k=0:7
        fgetl(config);
    end
    fis.Rules = [];
    rules = [];
    for k=1:27
        rule = fscanf(config,'%f', [1, 7]);
        rules = [rules; rule];
    end
    
    fis = addRule(fis,rules);
    writeFIS(fis,"TempSolution");
    
    exit
end

