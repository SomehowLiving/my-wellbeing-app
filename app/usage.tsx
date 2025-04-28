import { NativeModules, Button, View, Text, FlatList, StyleSheet } from 'react-native';
import { useState } from 'react';

const { DigitalWellbeing } = NativeModules;

export default function UsageScreen() {
  const [usageStats, setUsageStats] = useState<any[]>([]);

  const requestPermission = () => {
    DigitalWellbeing.openUsageAccessSettings();
  };

  const fetchUsageStats = () => {
    DigitalWellbeing.getUsageStats()
      .then((stats: any[]) => {
        console.log('Stats:', stats);
        setUsageStats(stats);
      })
      .catch((err: any) => console.error(err));
  };
  

  return (
    <View style={styles.container}>
      <Button title="Open Usage Access Settings" onPress={requestPermission} />
      <View style={{ marginVertical: 10 }} />
      <Button title="Fetch Usage Stats" onPress={fetchUsageStats} />
      
      <FlatList
        data={usageStats}
        keyExtractor={(item, index) => item.packageName + index}
        renderItem={({ item }) => (
          <View style={styles.item}>
            <Text style={styles.text}>App: {item.packageName}</Text>
            <Text style={styles.text}>Foreground Time: {Math.round(item.totalTimeInForeground / 60000)} mins</Text>
          </View>
        )}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginTop: 50,
    padding: 20,
    flex: 1,
  },
  item: {
    marginBottom: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#ccc',
    paddingBottom: 10,
  },
  text: {
    fontSize: 16,
  },
});
