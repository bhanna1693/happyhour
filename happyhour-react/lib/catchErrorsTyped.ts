export function catchErrorTyped<T, E extends new (...args: unknown[]) => Error>(
  promise: Promise<T>,
  errorsToCatch?: E[] // optional array of errors to catch
): Promise<[result: T | undefined, error: InstanceType<E> | undefined]> {
  return promise
    .then((data) => [data, undefined] as [T, undefined])
    .catch((error) => {
      // If specific error types are passed, check if the error is one of them
      if (errorsToCatch) {
        const isCaughtError = errorsToCatch.some(
          (errorType) => error instanceof errorType
        );
        if (isCaughtError) {
          return [undefined, error] as [undefined, InstanceType<E>];
        }
      }
      // If no specific errorsToCatch or error is not one of them, rethrow
      throw error;
    });
}
