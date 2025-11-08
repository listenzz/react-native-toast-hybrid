import { TurboModuleRegistry } from 'react-native';
import type { CodegenTypes, TurboModule } from 'react-native';

export interface Spec extends TurboModule {
   config: (options: CodegenTypes.UnsafeObject) => void;
   createToast: () => Promise<number>;
   hide: (key: number) => void;
   ensure: (key: number) => Promise<number>;
   loading: (key: number, text?: string) => void;
   text: (key: number, text: string) => void;
   info: (key: number, text: string) => void;
   done: (key: number, text: string) => void;
   error: (key: number, text: string) => void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('ToastHybrid');