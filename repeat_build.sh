while true; do
    echo "Starting build process..."
    ./gradlew clean build -i
    if [ $? -ne 0 ]; then
        echo "Build failed. Stopping script."
        break;
    fi
    echo "Build success Retrying..."
done
