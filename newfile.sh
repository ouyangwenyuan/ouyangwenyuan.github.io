month=$1
sudo date ${month}08272025.03

# if [ $# -eq 0 ]; then
    FileName=`date "+%Y-%m-%d"`
    hexo new $FileName
# fi