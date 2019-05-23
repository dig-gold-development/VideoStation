#!/bin/bash
# 这个东西是用来区分脚本的类型的
# 自动检查APk和瘦身


loseWeightApk(){

    #运行命令
    echo ""
    echo ""


    echo -e "\033[33m=======================================================================\033[0m"
    echo -e "\033[33m==============================运行检查命令=============================\033[0m"
    echo -e "\033[33m=======================================================================\033[0m"
     # fileSize min 只是读取5kb一直上的

    java -jar matrix-apk-canary-0.5.1.jar \
     --apk $apkPath \
     --format mm.html,mm.json \
     -manifest \
     -fileSize --min 5 --order desc --suffix png,jpg,jpeg,gif,arsc \
     -countMethod --group package \
     -checkResProguard \
     -findNonAlphaPng --min 10 \
     -checkMultiLibrary \
     -uncompressedFile --suffix png,jpg,jpeg,gif,arsc \
     -countR \
     -duplicatedFile \
     -unusedAssets --ignoreAssets *.so \
     -unstrippedSo --toolnm $ANDROID_NDK/toolchains/arm-linux-androideabi-4.9/prebuilt/darwin-x86_64/bin/arm-linux-androideabi-nm \
     -checkMultiSTL --toolnm $ANDROID_NDK/toolchains/arm-linux-androideabi-4.9/prebuilt/darwin-x86_64/bin/arm-linux-androideabi-nm \

     #把这方法工程中间去
    #  -unusedResource --rTxt /Users/williamjin/SampleApplication/app/build/intermediates/symbols/release/R.txt --ignoreResources R.raw.*,R.style.*,R.attr.*,R.id.*,R.string.ignore_*
        #  --mappingTxt:/Users/williamjin/SampleApplication/app/build/outputs/mapping/release/mapping.txt \
    #  --resMappingTxt:/Users/williamjin/SampleApplication/app/build/outputs/apk/release/AndResGuard_app-release-unsigned/resource_mapping_app-release-unsigned.txt \
    #  --output:/Users/williamjin/SampleApplication/app/build/outputs/apk-checker-result \

    echo "======================================================================="
    echo "================================= 完成 ================================"
    echo "======================================================================"
}


# 打印当前路径
currentPath=$(pwd)

if [[ $1 == "" ]] #需要2层[[]] 因为$1 可能为空
then 
    # echo -e "\033[31m重新运行，请输入Apkname，例如:\033[0m"
    # echo -e "\033[31m$0 test.apk\033[0m"
    read -p "选择release(r) or debug(d) " VERSSION
    if [[ VERSSION == "r" ]]
    then
        apkPath=$currentPath/build/outputs/apk/release/app-release.apk
    else
        apkPath=$currentPath/build/outputs/apk/debug/app-debug.apk
    fi
    echo "当前完整路径: $apkPath"
else #有Apk的路径
    echo "当前完整路径: $currentPath/$1"
    apkPath=$currentPath/$1
fi
loseWeightApk

# read -p "Please input a filename1:" MY_FILE_NAME1
# java -jar matrix-apk-canary-0.5.1.jar --config config_file.json

