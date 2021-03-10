
%% Initial SET
fis = readfis('ComnetUniformSetsV2');

    %% Input 1
    [xOut,yOut] = plotmf(fis,'input',1);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Initial Set')
    xlabel('RssI')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/RSSI_InitialSet.png')

    close(f1)

    %% Input 2
    [xOut,yOut] = plotmf(fis,'input',2);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Initial Set')
    xlabel('Number of neighobors APs')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/NumberAPs_InitialSet.png')
    
    close(f1)

    %% Input 3
    [xOut,yOut] = plotmf(fis,'input',3);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Initial Set')
    xlabel('Number of conected sensors')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/NumberSensors_InitialSet.png')

    close(f1)
    
    %% Input 4
    [xOut,yOut] = plotmf(fis,'input',4);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Initial Set')
    xlabel('Throughput')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')
    

    saveas(f1, '/home/jaevillen/IC/Results/Throughput_InitialSet.png')

    close(f1)
    
    %% Output 
    [xOut,yOut] = plotmf(fis,'output',1);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Initial Set')
    xlabel('Switch Off Percentage')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/Output_InitialSet.png')

    close(f1)



%% Sc1 AG Set

fis = readfis('AGSetsSc1');

    %% Input 1
    [xOut,yOut] = plotmf(fis,'input',1);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 1 AG Set')
    xlabel('RssI')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/RSSI_Sc1AGSet.png')

    close(f1)

    %% Input 2
    [xOut,yOut] = plotmf(fis,'input',2);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 1 AG Set')
    xlabel('Number of neighobors APs')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/NumberAPs_Sc1AGSet.png')
    
    close(f1)

    %% Input 3
    [xOut,yOut] = plotmf(fis,'input',3);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 1 AG Set')
    xlabel('Number of conected sensors')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/NumberSensors_Sc1AGSet.png')

    close(f1)
    
    %% Input 4
    [xOut,yOut] = plotmf(fis,'input',4);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 1 AG Set')
    xlabel('Throughput')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/Throughput_Sc1AGSet.png')

    close(f1)
    
    %% Output 
    [xOut,yOut] = plotmf(fis,'output',1);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 1 AG Set')
    xlabel('Switch Off Percentage')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/Output_Sc1AGSet.png')

    close(f1)

%% Sc2 AG Set
fis = readfis('AGSetsSc2');

    %% Input 1
    [xOut,yOut] = plotmf(fis,'input',1);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 2 AG Set')
    xlabel('RssI')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/RSSI_Sc2AGSet.png')

    close(f1)

    %% Input 2
    [xOut,yOut] = plotmf(fis,'input',2);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 2 AG Set')
    xlabel('Number of neighobors APs')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/NumberAPs_Sc2AGSet.png')
    
    close(f1)

    %% Input 3
    [xOut,yOut] = plotmf(fis,'input',3);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 2 AG Set')
    xlabel('Number of conected sensors')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/NumberSensors_Sc2AGSet.png')

    close(f1)
    
    %% Input 4
    [xOut,yOut] = plotmf(fis,'input',4);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 2 AG Set')
    xlabel('Throughput')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/Throughput_Sc2AGSet.png')

    close(f1)
    
    %% Output 
    [xOut,yOut] = plotmf(fis,'output',1);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 2 AG Set')
    xlabel('Switch Off Percentage')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/Output_Sc2AGSet.png')

    close(f1)

%% Sc3 AG Set

fis = readfis('AGSetsSc3');

    %% Input 1
    [xOut,yOut] = plotmf(fis,'input',1);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 3 AG Set')
    xlabel('RssI')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/RSSI_Sc3AGSet.png')

    close(f1)

    %% Input 2
    [xOut,yOut] = plotmf(fis,'input',2);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 3 AG Set')
    xlabel('Number of neighobors APs')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/NumberAPs_Sc3AGSet.png')
    
    close(f1)

    %% Input 3
    [xOut,yOut] = plotmf(fis,'input',3);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 3 AG Set')
    xlabel('Number of conected sensors')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/NumberSensors_Sc3AGSet.png')

    close(f1)
    
    %% Input 4
    [xOut,yOut] = plotmf(fis,'input',4);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 3 AG Set')
    xlabel('Throughput')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/Throughput_Sc3AGSet.png')

    close(f1)
    
    %% Output 
    [xOut,yOut] = plotmf(fis,'output',1);

    f1 = figure; 
    plot(xOut,yOut,'LineWidth',2);
    title('Scenario 3 AG Set')
    xlabel('Switch Off Percentage')
    ylabel('Degree of membership')
    legend('Low', 'Medium', 'High')

    saveas(f1, '/home/jaevillen/IC/Results/Output_Sc3AGSet.png')

    close(f1)

