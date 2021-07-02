import React, { useEffect, useRef, useState } from 'react'
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native'
import Toast, { useToast } from 'react-native-toast-hybrid'

export default function App({ navigator }) {
  const timerRef = useRef()

  const [count, setCount] = useState(0)

  useEffect(() => {
    Toast.config({
      // backgroundColor: '#BB000000',
      // tintColor: '#FFFFFF',
      // fontSize: 16,
      // cornerRadius: 5, // only for android
      // duration: 2000,
      // graceTime: 300,
      // minShowTime: 500,
      // dimAmount: 0.0, // only for andriod
      loadingText: 'Loading...',
    })

    return () => {
      if (timerRef.current) {
        clearTimeout(timerRef.current)
      }
    }
  }, [])

  useEffect(() => {
    navigator.isStackRoot().then(isRoot => {
      if (!isRoot) {
        setTimeout(() => {
          navigator.pop()
        }, 2000)
      }
    })
  }, [navigator])

  const toast = useToast()

  const loading = () => {
    toast.loading()
    setCount(count => count + 1)
    timerRef.current = setTimeout(() => {
      toast.done('Work is done!')
      timerRef.current = setTimeout(() => {
        toast.loading('New task in progress...')
        timerRef.current = setTimeout(() => {
          timerRef.current = undefined
          toast.hide()
        }, 2000)
      }, 1500)
    }, 2000)
  }

  console.log(`count ${count}`)

  const text = () => {
    Toast.text('Hello World!!')
    // toast.text('Hello World!!')
  }

  const info = () => {
    toast.info('A long long message to tell you, A long long message to tell you, A long long message to tell you')
  }

  const done = () => {
    toast.done('Work is Done！')
  }

  const error = () => {
    toast.error('Maybe somthing is wrong！')
  }

  const push = () => {
    navigator.push('Tab1')
  }

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={loading} activeOpacity={0.2} style={styles.button}>
        <Text style={styles.buttonText}> loading </Text>
      </TouchableOpacity>

      <TouchableOpacity onPress={text} activeOpacity={0.2} style={styles.button}>
        <Text style={styles.buttonText}> text </Text>
      </TouchableOpacity>

      <TouchableOpacity onPress={info} activeOpacity={0.2} style={styles.button}>
        <Text style={styles.buttonText}> info </Text>
      </TouchableOpacity>

      <TouchableOpacity onPress={done} activeOpacity={0.2} style={styles.button}>
        <Text style={styles.buttonText}> done </Text>
      </TouchableOpacity>

      <TouchableOpacity onPress={error} activeOpacity={0.2} style={styles.button}>
        <Text style={styles.buttonText}> error </Text>
      </TouchableOpacity>

      <TouchableOpacity onPress={push} activeOpacity={0.2} style={styles.button}>
        <Text style={styles.buttonText}> push and auto pop back </Text>
      </TouchableOpacity>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'stretch',
    paddingTop: 16,
  },

  button: {
    alignItems: 'center',
    justifyContent: 'center',
    height: 40,
  },

  buttonText: {
    backgroundColor: 'transparent',
    color: 'rgb(34,88,220)',
  },

  text: {
    backgroundColor: 'transparent',
    fontSize: 16,
    alignSelf: 'flex-start',
    textAlign: 'left',
    margin: 8,
  },
})
