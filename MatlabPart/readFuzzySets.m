function readFuzzySets
    fid = fopen('/home/jaevillen/IC/Buffer/ConfigFile.txt','r');
    fgetl(fid); %% Reads the name
    fgetl(fid); %% Reads configurations ... 
    upperLimits = fscanf(fid,'%i', [1, 5]);
    lowerLimits = fscanf(fid,'%i', [1, 5]);
    variableNames = ["rssi", "neighbors", "sources", "throughput", "switch_perc"];
    
    for k = 0:6 %% The first individuo is already evaluated
       fgetl(fid);
    end       
    for k = 0:8 %% The 9 new individuos
        name = fscanf(fid,'%s', [1, 2]); %% Reads the subject's name
        fis = mamfis;
        fis = addInput(fis,[lowerLimits(1) upperLimits(1)],"Name","rssi");
        fis = addInput(fis,[lowerLimits(2) upperLimits(2)],"Name","neighbors");
        fis = addInput(fis,[lowerLimits(3) upperLimits(3)],"Name","sources");
        fis = addInput(fis,[lowerLimits(4) upperLimits(4)],"Name","throughput");
        fis = addOutput(fis,[lowerLimits(5) upperLimits(5)],"Name","switch_perc");
        
        for variable = variableNames %% The five variables, includind the one output    
            A = fscanf(fid,'%f', [1, 3]);
            B = fscanf(fid,'%f', [1, 3]);
            C = fscanf(fid,'%f', [1, 3]);
            fis = addMF(fis,variable,"trimf",A);
            fis = addMF(fis,variable,"trimf",B);
            fis = addMF(fis,variable,"trimf",C);
        end
        writeFIS(fis,name);

    end     
end