mhmys=$1
sudo date ${mhmys}

# if [ $# -eq 0 ]; then
    FileName=`date "+%Y-%m-%d"`
    hexo new $FileName
# fi
